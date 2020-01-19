#! /bin/sh

GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
COLORLESS='\033[0m'

pFailure()
{
    echo -e "$RED$1$COLORLESS"
}

pSuccess()
{
    echo -e "$GREEN$1$COLORLESS"
}

pInfo()
{
    echo -e "$BLUE$1$COLORLESS"
}

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

pathRes=$pathRootProject$pathTests$pathProvided"/res"

echo
pInfo "## CONDEGEN VALID ##"
echo

for i in $(find . -name "*.deca")
# Iterate over all file .deca in sub-repository
do 

    nameFile=$(basename "$i" .deca)
    decac $i > $LOGS/$nameFileOutput.compilation.output 2> $LOGS/$nameFileOutput.compilation.error
    # Redirect the standard output to log .output
    # Redirect the error output to log .error

    if [ -s $LOGS/$nameFileOutput.compilation.output ]
    # Check if the output file is empty (empty by default)
    then
        pFailure "OUTPUT DETECTED"
        pFailure "SHOW LOGS FOR $nameFile"
        pFailure "$LOGS/$nameFileOutput.compilation.error"
        pFailure "$LOGS/$nameFileOutput.compilation.output"

      #  exit 1
    fi
    # No output detected

    if [ -s $LOGS/$nameFileOutput.error ]
    # Check for errors in the compilation
    then
        pFailure "ERROR DETECTED"
        pFailure "SHOW LOGS FOR $nameFile"
        pFailure "$LOGS/$nameFileOutput.compilation.error"
        pFailure "$LOGS/$nameFileOutput.compilation.output"

        #exit 1
    fi
    # No error detected

    pSuccess "OK COMPILATION \t $nameFile"
    #rm $LOGS/$nameFileOutput.*

    ima ${i%.*}.ass >  $pathRes/$nameFile.res 
    # Execute the file and store the output and error


    #rm ${i%.*}.ass

done

exit 0