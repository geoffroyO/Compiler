#! /bin/sh

## Souleve des erreurs interne du compilateur

## Les tests sont contenus dans le fichier internal error
## Chaque sous dossier contient des fichiers .deca ainsi que error.find 
## qui contient le type d'erreur soulevee
## Deux fichiers sont generes a chaque fois dans les LOGS
## Un fichier contenant la sortie <type><heure>.output
## Un fichier contenant les erreurs <type><heure>.error

## En cas de reussite les fichier de logs sont supprimes 
## Si une erreur est detecte, le script s'arrête avec un exit 1

## Les fichiers doivent avoir une sortie vide
## Les fichiers doivent contenir l'erreur dans le log d'erreur


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
pathTests='/src/test/deca/internalError'

LOGS=$pathRootProject$pathLogs
mkdir $LOGS #Create LOGS folder

# On se place dans le repertoire de test
cd $pathRootProject$pathTests

for i in $(find . -maxdepth 1 -mindepth 1 -type d)
# Iterate over the folder
do

    cd $i

    typeError=$(cat error.find)

    echo 
    pInfo "$typeError"
    echo

    for v in $(find . -name "*.deca")
    do

        cTime="`date "+%m_%d_%H_%M_%S"`"
        nameFile=$v.$cTime

        decac $v > $LOGS/$nameFile.output 2> $LOGS/$nameFile.error

        if [ -s $LOGS/$nameFile.output ]
        # Check if the file is empty (ie no output by default)
        then
            pFailure "OUTPUT FILE NOT EMPTY"
            pFailure "SHOW LOGS FOR $nameFile"
            pFailure "$LOGS/$nameFile.error"
            pFailure "$LOGS/$nameFile.output"
            exit 1
        fi

        if [ -s $LOGS/$nameFile.error ]
        # Check if the file is empty (ie no output by default)
        then
            pSuccess "Error Find \t $v"
            rm $LOGS/$nameFile.*
        else
            pFailure "NO ERROR FOUND"
            pFailure "SHOW LOGS FOR $nameFile"
            pFailure "$LOGS/$nameFile.error"
            pFailure "$LOGS/$nameFile.output"
            exit 1
        fi
    
    done

    cd ..

done 



exit 0