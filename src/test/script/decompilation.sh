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


## Function decompilation
decompilation()
# $1 arg -> input file name
# $2 arg -> output file name
{
    decac $1 -p > $2
}

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :

cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/deca/context/valid/created
pathLogs=../../../../logs

cd $path
mkdir $pathLogs

echo "## DECOMPILATION ##"
echo


for i in *.deca
do 
    decompilation $i $pathLogs/$i.v1
    decompilation $pathLogs/$i.v1 $pathLogs/$i.v2

    valDiff=$(diff -q $pathLogs/$i.v1 $pathLogs/$i.v2 2>&1)

    if ["$valDiff" = ""] 
    then
        pSuccess "DECOMPILATION $i"
        rm $pathLogs/$i.v*

    else
        pFailure "DECOMPILATION KO"
        pFailure "FICHIER $i"
        pFailure "LOGS $i.v1 $i.v2"

        exit 1
    fi

done

exit 0