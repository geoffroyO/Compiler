#! /bin/sh

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/deca/syntax/valid/created #Path execution script
pathLogs=../../../../logs

cd $path || exit 1
mkdir $pathLogs

echo "###########################"
echo "## PARSER NON REGRESSION ##"
echo "###########################"

echo

for i in *.deca
do
    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    test_synt $i > $pathLogs/$i.$cDate.parser.logs

    valDiff=$(diff -q $pathLogs/$i.$cDate.parser.logs parser/$i.res)

    if ["$valDiff" = ""] 
    then

        echo "\e[32mVALID \t $i.deca\e[39m"
        rm $pathLogs/$i.$cDate.parser.logs

    else
        echo "\e[31mINVALIDE\e[39m"
        echo "\e[31mERROR IN FILE $i\e[39m"
        echo "\e[31mSHOW LOGS $i.$cDate.parser.logs\e[39m"
        exit 1
    fi

done

cd ../../invalid/created

for i in *.deca
do
    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    test_synt $i > $pathLogs/$i.$cDate.parser.logs

    valDiff=$(diff -q $pathLogs/$i.$cDate.parser.logs parser/$i.res)

    if ["$valDiff" = ""] 
    then
        echo "\e[32mVALID \t $i.deca\e[39m"
        rm $pathLogs/$i.$cDate.parser.logs

    else
        echo "\e[31mINVALID\e[39m"
        echo "\e[31mERROR IN FILE $i\e[39m"
        echo "\e[31mSHOW LOGS $i.$cDate.parser.logs\e[39m"
        exit 1
    fi

done
