#! /bin/sh

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

# Test N fichiers
# Tous les fichiers crées sont invalides

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/script
pathLog=./src/test

#Create the logs directory
mkdir $pathLog/logs

x=1
n=10 # Number of tests

name="A_lexer_invalid"

echo
echo "## Etape A _ Test LEXER ##"
echo "Generation aleatoire LEXER INVALIDE"
echo 

while [ $x -le $n ]
do
    
    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    common=$name$cDate.$x
    nameDeca=$common.deca
    nameLog=$common.log

    python $path/generator/lexerInvalid.py $nameDeca > $pathLog/logs/$nameLog

    if echo $(cat $pathLog/logs/$nameLog) 2>&1 | grep -q -e 'OK'
    then
        pSuccess "DETECTE INVALIDE"
        rm $pathLog/logs/$common.*

    else

        #Stop the test and display the error
        pFailure "DETECTE VALIDE"
        pFailure "CF fichier log $nameLog"

        #Stop the test 
        exit 1
    fi

    x=$(( $x + 1 ))

done

exit 0