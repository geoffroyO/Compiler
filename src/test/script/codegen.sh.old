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


path=./src/test/deca/codegen/valid/created
pathLogs=../../../../logs

cd $path
mkdir $pathLogs


echo "## CODEGEN ##"
echo

for i in *.deca
do 
    cDate="`date "+%Y_%m_%d_%H_%M_%S"`"

    decac $i
    ima ${i%.*}.ass > $pathLogs/$i.codegen.$cDate.logs  # Remove .deca from .ass file

    rm ${i%.*}.ass

    valDiff=$(diff -q $pathLogs/$i.codegen.$cDate.logs res/$i.res 2>&1)

    if ["$valDiff" = ""] 
    then
        pSuccess "CODEGEN $i"
        rm $pathLogs/$i.codegen.$cDate.logs

    else
        pFailure "CODEGEN KO"
        pFailure "FICHIER $i"
        pFailure "LOGS $pathLogs/$i.codegen.$cDate.logs"

        exit 1
    fi

done


exit 0

