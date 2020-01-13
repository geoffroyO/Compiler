#! /bin/sh

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

    python $path/stepA/lexerValid.py $nameDeca > $pathLog/logs/$nameLog

    if echo $(cat $pathLog/logs/$nameLog) 2>&1 | grep -q -e 'OK'
    then
        echo "\e[32mDETECTE VALIDE\e[39m"
        rm $pathLog/logs/$common.*
    else

        #Stop the test and display the error
        echo "\e[31mDETECTE INVALIDE\e[39m"
        echo "\e[31mCF fichier log $nameLog\e[39m"
        exit 1
    fi

    x=$(( $x + 1 ))
done

exit 0