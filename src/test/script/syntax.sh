#! /bin/sh

## Verifie les tests de l'etape A

## Tous les fichiers .deca sont appeles avec decac -p
## Deux fichiers sont generes a chaque fois dans les LOGS
## Un fichier contenant la sortie <nomFichier><heure>.output
## Un fichier contenant les erreurs <nomFichier><heure>.error

## En cas de reussite les fichier de logs sont supprimes 
## Si une erreur est detecte, le script s'arrête avec un exit 1

## Fichier valid
## Les fichiers valides sont dans le sous dossier valid.
## Les fichiers ne doivent pas contenir d'erreur

## Fichier invalid
## Les fichiers invalides sont dans le sous dossier invalid.
## Tous les fichiers invalides possedent en commentaire le numero de ligne soulevant l'erreur
## Le fichier de sortie doit contenir l'erreur au bon format 
## L'erreur doit être à la bonne ligne
## Le fichier de sortie doit être vide


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
pathTests='/src/test/deca/syntax'

LOGS=$pathRootProject$pathLogs
mkdir $LOGS #Create LOGS folder

# On se place dans le repertoire de l'etape A
cd $pathRootProject$pathTests

# Syntax Valid
cd "./valid/created"

echo
pInfo "## SYNTAX VALID ##"
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

    decac -p $i > $LOGS/$nameFileOutput.output 2> $LOGS/$nameFileOutput.error
    # Redirect the standard output to log .output
    # Redirect the error output to log .error

    if [ -s $LOGS/$nameFileOutput.error ]
    # Check if the file is empty (ie no error detected)
    then
        pFailure "ERROR DETECTED"
        pFailure "SHOW LOGS FOR $nameFile"
        pFailure "$LOGS/$nameFileOutput.error"
        pFailure "$LOGS/$nameFileOutput.output"

        exit 1
    fi

    pSuccess "NO ERROR FOUND \t $nameFile"
    rm $LOGS/$nameFileOutput.*
    # Delete the logs

done

cd '../../'
cd "./invalid/created"

echo
pInfo "## SYNTAX INVALID ##"
echo

# The output of -p should be empty
# The error output should not be empty
# The error should contains the file name

for i in $(find  -name "*.deca")
# Iterate over all file .deca in sub-repository
do 

    cTime="`date "+%m_%d_%H_%M_%S"`"
    # Current time

    nameFile=$(basename "$i" .deca)
    nameFileOutput=$nameFile"_$cTime"

    # get only the name of the file, without .deca and path
    # add the current time


    decac -p $i > $LOGS/$nameFileOutput.output 2> $LOGS/$nameFileOutput.error
    # Redirect the standard output to log .output
    # Redirect the error output to log .error

    lineError=$(python $pathRootProject/src/test/script/findLineError.py $i)
    # Use the convention Ligne : <nbLigne> : Erreur

    if [ -s $LOGS/$nameFileOutput.output ]
    # Check if the output file is empty
    then
        pFailure "OUTPUT FILE NOT EMPTY"
        pFailure "SHOW LOGS FOR $nameFile"
        pFailure "$LOGS/$nameFileOutput.error"
        pFailure "$LOGS/$nameFileOutput.output"

        exit 1
    fi

    if (cat $LOGS/$nameFileOutput.error | grep -q -e "$nameFile.deca:$lineError")
    # Compare the error find with the error expected
    then 
        pSuccess "ERROR FOUND \t $nameFile"
        rm $LOGS/$nameFileOutput.*
    else 

        #Error not find
        #But can be circular include because the file name is .decah != $namefile

        if ((cat $LOGS/$nameFileOutput.error | grep -q -e ".decah:$lineError")&(cat $LOGS/$nameFileOutput.error | grep -q -e "Circular include"))
        then 
            pSuccess "FOUND Circular include \t $nameFile"
            rm $LOGS/$nameFileOutput.*
        else

            pFailure "ERROR NOT FOUND \t $nameFile"
            pFailure "SHOW LOGS"
            pFailure "$LOGS/$nameFileOutput.error"
            pFailure "$LOGS/$nameFileOutput.output"

            exit 1

        fi
    fi

done

exit 0
