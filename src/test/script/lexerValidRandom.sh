#! /bin/sh

## Test le lexer avec des fichiers valide generes aleatoirement

## Les tests sont generes aleatoirement avec des scripts python situe dans le sous repertoire generator
## Deux fichiers sont generes a chaque fois dans les LOGS
## Un fichier contenant la sortie <nom><heure>.output
## Un fichier contenant les erreurs <nom><heure>.error

## En cas de reussite les fichier de logs sont supprimes 
## Si une erreur est detecte, le script s'arrête avec un exit 1

## Tous les fichiers doivent etre detectes valide


GREEN='\033[0;32m'
RED='\033[0;31m'
COLORLESS='\033[0m'

pFailure()
{
    echo -e "$RED $1 $COLORLESS"
}

pSuccess()
{
    echo -e "$GREEN $1 $COLORLESS"
}

# Test N fichiers .deca aléatoirement 
# Tous les fichiers crées avec des Tokens valide

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/script #Path execution script
pathLog=./src/test

#Create the logs directory (if it doesn't exist already)
mkdir $pathLog/logs

x=1
n=10 # Number of tests

name="A_lexer_valid_" #Prefix of the output file

echo
echo "## Etape A _ Test LEXER ##"
echo "Generation aleatoire LEXER VALIDE"
echo 

while [ $x -le $n ]
do


    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    common=$name$cDate.$x
    nameDeca=$common.deca
    nameLog=$common.log

    python $path/generator/lexerValid.py $nameDeca > $pathLog/logs/$nameLog

    if echo $(cat $pathLog/logs/$nameLog) 2>&1 | grep -q -e 'OK'
    then
        pSuccess "DETECTE VALIDE"
        rm $pathLog/logs/$common.*
    else

        #Stop the test and display the error
        pFailure "DETECTE INVALIDE"
        pFailure "CF fichier log $nameLog"
        exit 1
    fi

    x=$(( $x + 1 ))
done

exit 0