#! /bin/sh

## List all the test in codegen
## Use to check if 2 tests have the same name (file.res need to be unique)
## Output can be export to calc for example

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :

cd "$(dirname "$0")"/../../.. || exit 1

pathRootProject=$(pwd)
pathLogs='./src/test/logs/'
pathTests='./src/test/deca/codegen'

LOGS=$pathRootProject$pathLogs
mkdir $LOGS #Create LOGS folder

# On se place dans le repertoire de l'etape C
cd $pathRootProject$pathTests

# CODEGEND Valid
pathProvided="./valid/created"
cd $pathProvided


for i in $(find . -name "*.deca")
# Iterate over all file .deca in sub-repository
do 

    nameFile=$(basename "$i" .deca)
    echo $nameFile >> $LOGS/res.txt

done

exit 0