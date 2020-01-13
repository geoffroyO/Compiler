#! /bin/sh


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

    valDiff=$(diff -q $pathLogs/$i.v1 $pathLogs/$i.v2)

    if ["$valDiff" = ""] 
    then

        echo "\e[32mDECOMPILATION $i\e[39m"
        rm $pathLogs/$i.v*

    else

        echo "\e[31mDECOMPILATION KO"
        echo "FICHIER $i"
        echo "LOGS $i.v1 $i.v2"
        echo "\e[39m"

        exit 1
    fi

done

exit 0