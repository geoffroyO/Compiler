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

path=./src/test/deca/syntax/valid/provided
cd $path


for i in *.deca
do 

    decompilation $i $i.v1
    decompilation $i.v1 $i.v2

    valDiff=$(diff -q $i.v1 $i.v2)

    if ["$valDiff" = ""] 
    then
        echo "OK"
        rm $i.v*
    else
        echo "KO"
        # Keep the files
        exit 1
    fi

done

exit 0