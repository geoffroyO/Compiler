#! /bin/sh

# Test N fichiers 
# Tous les fichiers crées sont valides

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/script

#Create the logs directory
mkdir $path/logs

x=1
n=2 # Number of tests

name="A_lexer_valid"

echo
echo "## Etape A _ Test LEXER ##"
echo "Generation LEXER VALIDE"
echo 

while [ $x -le $n ]
do

    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    python $path/stepA/lexerValid.py > $path/logs/$name$x.$cDate.log

    if echo $(cat $path/logs/$name$x.$cDate.log) 2>&1 | grep -q -e 'OK'
    then
        echo "\e[32mDETECTE VALIDE\e[39m"
    else

        #Stop the test and display the error
        echo "\e[31mDETECTE INVALIDE\e[39m"
        echo "\e[31mCF fichier log $name$x.$cDate.log\e[39m"
        #Don't delete the log file

        exit 1
    fi

    rm $path/logs/$name$x.$cDate.log
    x=$(( $x + 1 ))
done
