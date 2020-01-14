#! /bin/sh

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/deca/syntax/valid/created #Path execution script
pathLogs=../../../../logs

cd $path || exit 1
mkdir $pathLogs

echo "##########################"
echo "## LEXER NON REGRESSION ##"
echo "##########################"


for i in *.deca
do
    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    test_lex $i > $pathLogs/$i.$cDate.lex.logs 2>&1 # Redirect stdErr 2 in stdOut 1

    valDiff=$(diff -q $pathLogs/$i.$cDate.lex.logs lexer/$i.res)

    if ["$valDiff" = ""] 
    then

        echo "\e[32mVALID \t $i.deca\e[39m"
        rm $pathLogs/$i.$cDate.lex.logs

    else
        echo "\e[31mDETECTE INVALIDE\e[39m"
        echo "\e[31mERROR IN FILE $i\e[39m"
        echo "\e[31mSHOW LOGS $i.$cDate.lex.logs\e[39m"
        exit 1
    fi

done

cd ../../invalid/created

for i in *.deca
do
    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    test_lex $i > $pathLogs/$i.$cDate.lex.logs 2>&1 # Redirect stdErr 2 in stdOut 1

    valDiff=$(diff -q $pathLogs/$i.$cDate.lex.logs lexer/$i.res)

    if ["$valDiff" = ""] 
    then
        echo "\e[32mVALID \t $i.deca\e[39m"
        rm $pathLogs/$i.$cDate.lex.logs

    else
        echo "\e[31mDETECTE VALID\e[39m"
        echo "\e[31mERROR IN FILE $i\e[39m"
        echo "\e[31mSHOW LOGS $i.$cDate.lex.logs\e[39m"
        exit 1
    fi

done
