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

    test_synt $i > $pathLogs/$i.$cDate.parser.logs 2>&1 # Redirect stdErr 2 in stdOut 1

    valDiff=$(diff -q $pathLogs/$i.$cDate.parser.logs parser/$i.res)

    if ["$valDiff" = ""] 
    then

        pSuccess "VALID \t $i.deca"
        rm $pathLogs/$i.$cDate.parser.logs

    else
        pFailure "INVALIDE"
        pFailure "ERROR IN FILE $i"
        pFailure "SHOW LOGS $i.$cDate.parser.logs"
        exit 1
    fi

done

cd ../../invalid/created

for i in *.deca
do
    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    test_synt $i > $pathLogs/$i.$cDate.parser.logs 2>&1 # Redirect stdErr 2 in stdOut 1

    valDiff=$(diff -q $pathLogs/$i.$cDate.parser.logs parser/$i.res)

    if ["$valDiff" = ""] 
    then
        pSuccess "VALID \t $i.deca"
        rm $pathLogs/$i.$cDate.parser.logs

    else
        pFailure 'INVALID'
        pFailure 'ERROR IN FILE $i'
        pFailure "SHOW LOGS $i.$cDate.parser.logs"
        exit 1
    fi

done
