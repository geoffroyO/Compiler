#! /bin/sh

## Verifie le parallelisme du compilateur

## Les tests sont contenus dans le fichier parallel
## Tous les fichiers .deca sont appeles avec decac -P (pour la gestion parallele)
## ainsi que sans pour une gestion iterative
## Deux fichiers sont generes a chaque fois dans les LOGS
## Un fichier contenant la sortie <type><heure>.output
## Un fichier contenant les erreurs <type><heure>.error

## En cas de reussite les fichier de logs sont supprimes 
## Si une erreur est detecte, le script s'arrête avec un exit 1

## Fichier valid
## Les fichiers valides sont dans le sous dossier valid.
## Les fichiers ne doivent pas contenir d'erreur

## Fichier invalid
## Les fichiers invalides sont dans le sous dossier invalid.
## Le fichier de sortie doit contenir l'erreur au bon format 
## L'erreur doit être à la bonne ligne
## Le fichier de sortie doit être vide


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

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/deca/parallel/valid/INT #Path execution 
pathLogs=../../../../logs

cd $path || exit 1
mkdir $pathLogs

pInfo "#################"
pInfo "## PARALLELISM ##"
pInfo "#################"

echo
pInfo "INTEGER VALID"
echo

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac -P *.deca > $pathLogs/integer.parallel.valid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/integer.parallel.valid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_INTEGER_ASSIGN_'
then
    # Error raise
    pFailure "ERROR DETECTED"
    pFailure "SHOW LOGS integer.parallel.valid.$cDate.logs"
    exit 1
else 
    pSuccess "OK"
    rm $pathLogs/integer.parallel.valid.$cDate.logs
fi

echo
echo "FLOAT VALID"
echo
cd ../FLOAT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac -P *.deca > $pathLogs/float.parallel.valid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/float.parallel.valid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_FLOAT_ASSIGN_'
then
    # Error raise
    pFailure "ERROR DETECTED"
    pFailure "SHOW LOGS float.parallel.valid.$cDate.logs"
    exit 1
else 
    pSuccess "OK"
    rm $pathLogs/float.parallel.valid.$cDate.logs
fi


echo
pInfo "INTEGER INVALID"
echo
cd ../../invalid/INT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac -P *.deca > $pathLogs/integer.parallel.invalid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/integer.parallel.invalid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_INTEGER_ASSIGN_19.deca:10002:'
then
    pSuccess "OK"
    rm $pathLogs/integer.parallel.invalid.$cDate.logs
else
    pFailure "ERROR DETECTED"
    pFailure "SHOW LOGS integer.parallel.invalid.$cDate.logs"
    exit 1
fi


echo
pInfo "FLOAT INVALID"
echo
cd ../FLOAT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac -P *.deca > $pathLogs/float.parallel.invalid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/float.parallel.invalid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_FLOAT_ASSIGN_19.deca:10002:'
then
    pSuccess "OK"
    rm $pathLogs/float.parallel.invalid.$cDate.logs
else
    pFailure "ERROR DETECTED"
    pFailure "SHOW LOGS float.parallel.invalid.$cDate.logs"
    exit 1
fi

echo
pInfo "################"
pInfo "## SEQUENTIAL ##"
pInfo "################"

echo
pInfo "INTEGER VALID"
echo
cd ../../valid/INT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac *.deca > $pathLogs/integer.sequential.valid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/integer.sequential.valid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_INTEGER_ASSIGN_'
then
    # Error raise
    pFailure "ERROR DETECTED"
    pFailure "SHOW LOGS integer.sequential.valid.$cDate.logs"
    exit 1
else 
    pSuccess "OK"
    rm $pathLogs/integer.sequential.valid.$cDate.logs
fi

echo
pInfo "FLOAT VALID"
echo
cd ../FLOAT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac *.deca > $pathLogs/float.sequential.valid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/float.sequential.valid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_FLOAT_ASSIGN_'
then
    # Error raise
    pFailure "ERROR DETECTED"
    pFailure "SHOW LOGS float.sequential.valid.$cDate.logs"
    exit 1
else 
    pSuccess "OK"
    rm $pathLogs/float.sequential.valid.$cDate.logs
fi


echo
pInfo "INTEGER INVALID"
echo
cd ../../invalid/INT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac *.deca > $pathLogs/integer.sequential.invalid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/integer.sequential.invalid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_INTEGER_ASSIGN_19.deca:10002:'
then
    pSuccess "OK"
    rm $pathLogs/integer.sequential.invalid.$cDate.logs
else
    pFailure "ERROR DETECTED"
    pFailure "SHOW LOGS integer.sequential.invalid.$cDate.logs"
    exit 1
fi


echo
pInfo "FLOAT INVALID"
echo
cd ../FLOAT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac *.deca > $pathLogs/float.sequential.invalid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/float.sequential.invalid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_FLOAT_ASSIGN_19.deca:10002:'
then
    pSuccess "OK"
    rm $pathLogs/float.sequential.invalid.$cDate.logs
else
    pFailure "ERROR DETECTED"
    pFailure "SHOW LOGS float.sequential.invalid.$cDate.logs"
    exit 1
fi

exit 0