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

pFailure() {
    echo -e "$RED$1$COLORLESS"
}

pSuccess() {
    echo -e "$GREEN$1$COLORLESS"
}

pInfo() {
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

for i in $(
    # Iterate over the folder
    find . -maxdepth 1 -mindepth 1 -type d
); do

    cd $i

    errorToFind=$(cat error.find)

    echo
    pInfo "$errorToFind"
    echo

    for v in $(find . -name "*.deca"); do

        cTime="$(date "+%m_%d_%H_%M_%S")"

        name=$(basename "$v" .deca)
        nameFile=$name.$cTime

        decac $name".deca" >$LOGS/$nameFile.output 2>$LOGS/$nameFile.error

        if (cat $LOGS/$nameFile.output | grep -q -e "$errorToFind" || cat $LOGS/$nameFile.error | grep -q -e "$errorToFind"); then
            # Compare the error find with the error expected
            pSuccess "ERROR FIND in COMPILATION \t $name"
            rm $LOGS/$nameFile.*
        else

            # Error not find at compile
            # Try to execute

            rm $LOGS/$nameFile.*
            ima $name".ass" >$LOGS/$nameFile.output 2>$LOGS/$nameFile.error

            if (cat $LOGS/$nameFile.output | grep -q -e "$errorToFind" || cat $LOGS/$nameFile.error | grep -q -e "$errorToFind"); then
                # Compare the error find with the error expected
                pSuccess "ERROR FIND in EXECUTION \t $name"
                rm $LOGS/$nameFile.*
                rm $name".ass"
            else

                pFailure "NO ERROR FOUND"
                pFailure "SHOW LOGS FOR $name"
                pFailure "$LOGS/$nameFile.error"
                pFailure "$LOGS/$nameFile.output"
                exit 1

            fi

        fi

    done

    cd ..

done

exit 0
