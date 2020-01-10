#! /bin/sh

# Test N fichiers
# Tous les fichiers crées sont invalides

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/script

#Create the logs directory
mkdir $path/logs

x=1
n=10 # Number of tests

name="A_lexer_invalid"

echo
echo "## Etape A _ Test LEXER ##"
echo "Generation LEXER INVALIDE"
echo 

while [ $x -le $n ]
do
    
    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    python $path/stepA/lexerInvalid.py > $path/logs/$name$x.$cDate.log

    if echo $(cat $path/logs/$name$x.$cDate.log) 2>&1 | grep -q -e 'OK'
    then
        echo "\e[32mDETECTE INVALIDE\e[39m"
    else

        #Stop the test and display the error
        echo "\e[31mDETECTE VALIDE\e[39m"
        echo "\e[31mCF fichier log $name$x.$cDate.log\e[39m"
        #Don't delete the log file

        #Stop the test 
        exit 1
    fi

    rm $path/logs/$name$x.$cDate.log
    x=$(( $x + 1 ))

done

