#! /bin/sh

## Verifie les tests de l'etape C

## Tous les fichiers .deca sont appeles avec decac 
## Puis avec ima
## Les fichiers de resultats de l'execution sont stockes dans le repertoire res

## A chaque fois, deux fichiers sont generes dans les LOGS
## Un fichier contenant la sortie <nomFichier><heure>.output
## Un fichier contenant les erreurs <nomFichier><heure>.error

## En cas de reussite les fichier de logs sont supprimes 
## Si une erreur est detecte, le script s'arrête avec un exit 1

## Fichier valid
## Les fichiers valides sont dans le sous dossier valid.

## Compilation
## Les fichiers ne doivent pas contenir d'erreur et avoir une sortie vide

## Compilation
## Les fichiers ne doivent pas contenir d'erreur et avoir une sortie egale a celui attendu dans res/<nom>.res


GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
COLORLESS='\033[0m'

pFailure()
{
    echo -e "$RED$1$COLORLESS"
}

pSuccess()
{
    echo -e "$GREEN$1$COLORLESS"
}

pInfo()
{
    echo -e "$BLUE$1$COLORLESS"
}

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :

cd "$(dirname "$0")"/../../.. || exit 1

pathRootProject=$(pwd)
pathLogs='/src/test/logs'
pathTests='/src/test/deca/codegen'

LOGS=$pathRootProject$pathLogs
mkdir $LOGS #Create LOGS folder

# On se place dans le repertoire de l'etape C
cd $pathRootProject$pathTests

# CODEGEND Valid
pathProvided="/valid/created"
cd '.'$pathProvided

pathRes=$pathRootProject$pathTests$pathProvided"/res"

echo
pInfo "## CONDEGEN VALID ##"
echo

for i in $(find . -name "*.deca")
# Iterate over all file .deca in sub-repository
do 

    cTime="`date "+%m_%d_%H_%M_%S"`"
    # Current time

    nameFile=$(basename "$i" .deca)
    nameFileOutput=$nameFile"_$cTime"

    # get only the name of the file, without .deca and path
    # add the current time

    decac $i > $LOGS/$nameFileOutput.compilation.output 2> $LOGS/$nameFileOutput.compilation.error
    # Redirect the standard output to log .output
    # Redirect the error output to log .error

    if [ -s $LOGS/$nameFileOutput.compilation.output ]
    # Check if the output file is empty (empty by default)
    then
        pFailure "OUTPUT DETECTED"
        pFailure "SHOW LOGS FOR $nameFile"
        pFailure "$LOGS/$nameFileOutput.compilation.error"
        pFailure "$LOGS/$nameFileOutput.compilation.output"

        exit 1
    fi
    # No output detected

    if [ -s $LOGS/$nameFileOutput.error ]
    # Check for errors in the compilation
    then
        pFailure "ERROR DETECTED"
        pFailure "SHOW LOGS FOR $nameFile"
        pFailure "$LOGS/$nameFileOutput.compilation.error"
        pFailure "$LOGS/$nameFileOutput.compilation.output"

        exit 1
    fi
    # No error detected

    pSuccess "OK COMPILATION \t $nameFile"
    rm $LOGS/$nameFileOutput.*

    ima ${i%.*}.ass > $LOGS/$nameFileOutput.execution.output 2> $LOGS/$nameFileOutput.execution.error
    # Execute the file and store the output and error

    if [ -s $LOGS/$nameFileOutput.execution.error ]
    # Check for errors
    then
        pFailure "ERROR DETECTED"
        pFailure "SHOW LOGS FOR $nameFile"
        pFailure "$LOGS/$nameFileOutput.execution.error"
        pFailure "$LOGS/$nameFileOutput.execution.output"

        exit 1
    fi
    # No error detected

    valDiff=$(diff -q $LOGS/$nameFileOutput.execution.output $pathRes/$nameFile.res 2>&1)
    # Check if the res is the right one

    if ["$valDiff" = ""] 
    then
        pSuccess "OK EXECUTION \t $nameFile"
        rm $LOGS/$nameFileOutput.*

    else
        pFailure "EXECUTION KO"
        pFailure "SHOW LOGS FOR $nameFile"
        pFailure "$LOGS/$nameFileOutput.execution.error"
        pFailure "$LOGS/$nameFileOutput.execution.output"

        exit 1
    fi

    rm ${i%.*}.ass

done

exit 0
