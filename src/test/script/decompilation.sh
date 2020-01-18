#! /bin/sh

GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
COLORLESS='\033[0m'

pFailure()
{
    echo -e "$RED $1 $COLORLESS"
}

pSuccess()
{
    echo -e "$GREEN $1 $COLORLESS"
}

pInfo()
{
    echo -e "$BLUE$1$COLORLESS"
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

pathRootProject=$(pwd)
pathLogs='/src/test/logs/'
pathTests='/src/test/deca'

LOGS=$pathRootProject$pathLogs
mkdir $LOGS #Create LOGS folder

# On se place dans le repertoire des tests
cd $pathRootProject$pathTests

pInfo "## DECOMPILATION ##"
echo

pInfo "Syntax valid"
echo

# Syntax Valid
cd "./syntax"
cd "./valid/created"

for i in $(find . -name "*.deca")
do 
    cTime="`date "+%m_%d_%H_%M_%S"`"
    # Current time

    nameFile=$(basename "$i" .deca)
    nameFileOutput=$nameFile"_$cTime"

    # get only the name of the file, without .deca and path
    # add the current time

    decompilation $i $LOGS/$nameFileOutput.v1
    decompilation $LOGS/$nameFileOutput.v1 $LOGS/$nameFileOutput.v2

    valDiff=$(diff -q $LOGS/$nameFileOutput.v1 $LOGS/$nameFileOutput.v2 2>&1)

    if ["$valDiff" = ""] 
    then
        pSuccess "DECOMPILATION $i"
        rm $LOGS/$nameFileOutput.v*

    else
        pFailure "DECOMPILATION KO"
        pFailure "FICHIER $i"
        pFailure "LOGS $LOGS/$nameFileOutput.v1"
        pFailure "LOGS $LOGS/$nameFileOutput.v2"

        exit 1
    fi

done

# Context Valid
cd "../../../"
cd "./context"
cd "./valid/created"

pInfo "Context valid"
echo

for i in $(find . -name "*.deca")
do 
    cTime="`date "+%m_%d_%H_%M_%S"`"
    # Current time

    nameFile=$(basename "$i" .deca)
    nameFileOutput=$nameFile"_$cTime"

    # get only the name of the file, without .deca and path
    # add the current time

    decompilation $i $LOGS/$nameFileOutput.v1
    decompilation $LOGS/$nameFileOutput.v1 $LOGS/$nameFileOutput.v2

    valDiff=$(diff -q $LOGS/$nameFileOutput.v1 $LOGS/$nameFileOutput.v2 2>&1)

    if ["$valDiff" = ""] 
    then
        pSuccess "DECOMPILATION $i"
        rm $LOGS/$nameFileOutput.v*

    else
        pFailure "DECOMPILATION KO"
        pFailure "FICHIER $i"
        pFailure "LOGS $LOGS/$nameFileOutput.v1"
        pFailure "LOGS $LOGS/$nameFileOutput.v2"

        exit 1
    fi

done

# Codegen Valid
cd "../../../"
cd "./codegen"
cd "./valid/created"

pInfo "Codegen valid"
echo

for i in $(find . -name "*.deca")
do 
    cTime="`date "+%m_%d_%H_%M_%S"`"
    # Current time

    nameFile=$(basename "$i" .deca)
    nameFileOutput=$nameFile"_$cTime"

    # get only the name of the file, without .deca and path
    # add the current time

    decompilation $i $LOGS/$nameFileOutput.v1
    decompilation $LOGS/$nameFileOutput.v1 $LOGS/$nameFileOutput.v2

    valDiff=$(diff -q $LOGS/$nameFileOutput.v1 $LOGS/$nameFileOutput.v2 2>&1)

    if ["$valDiff" = ""] 
    then
        pSuccess "DECOMPILATION $i"
        rm $LOGS/$nameFileOutput.v*

    else
        pFailure "DECOMPILATION KO"
        pFailure "FICHIER $i"
        pFailure "LOGS $LOGS/$nameFileOutput.v1"
        pFailure "LOGS $LOGS/$nameFileOutput.v2"

        exit 1
    fi

done

exit 0