#! /bin/sh

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

        echo "\e[32mCODEGEN $i\e[39m"
        rm $pathLogs/$i.codegen.$cDate.logs

    else

        echo "\e[31mCODEGEN KO"
        echo "FICHIER $i"
        echo "LOGS $pathLogs/$i.codegen.$cDate.logs"
        echo "\e[39m"

        exit 1
    fi

done


exit 0

