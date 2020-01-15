#! /bin/sh

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/deca/parallel/valid/INT #Path execution 
pathLogs=../../../../logs

cd $path || exit 1
mkdir $pathLogs

echo "#################"
echo "## PARALLELISM ##"
echo "#################"

echo
echo "INTEGER VALID"
echo

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac -P *.deca > $pathLogs/integer.parallel.valid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/integer.parallel.valid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_INTEGER_ASSIGN_'
then
    # Error raise
    echo "\e[31mERROR DETECTED\e[39m"
    echo "\e[31mSHOW LOGS integer.parallel.valid.$cDate.logs\e[39m"
else 
    echo "\e[32mOK\e[39m"
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
    echo "\e[31mERROR DETECTED\e[39m"
    echo "\e[31mSHOW LOGS float.parallel.valid.$cDate.logs\e[39m"
else 
    echo "\e[32mOK\e[39m"
    rm $pathLogs/float.parallel.valid.$cDate.logs
fi


echo
echo "INTEGER INVALID"
echo
cd ../../invalid/INT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac -P *.deca > $pathLogs/integer.parallel.invalid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/integer.parallel.invalid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_INTEGER_ASSIGN_19.deca:10002:'
then
    echo "\e[32mOK\e[39m"
    rm $pathLogs/integer.parallel.invalid.$cDate.logs
else
    echo "\e[31mERROR DETECTED\e[39m"
    echo "\e[31mSHOW LOGS integer.parallel.invalid.$cDate.logs\e[39m"
fi


echo
echo "FLOAT INVALID"
echo
cd ../FLOAT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac -P *.deca > $pathLogs/float.parallel.invalid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/float.parallel.invalid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_FLOAT_ASSIGN_19.deca:10002:'
then
    echo "\e[32mOK\e[39m"
    rm $pathLogs/float.parallel.invalid.$cDate.logs
else
    echo "\e[31mERROR DETECTED\e[39m"
    echo "\e[31mSHOW LOGS float.parallel.invalid.$cDate.logs\e[39m"
fi

echo
echo "################"
echo "## SEQUENTIAL ##"
echo "################"

echo
echo "INTEGER VALID"
echo
cd ../../valid/INT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac *.deca > $pathLogs/integer.sequential.valid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/integer.sequential.valid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_INTEGER_ASSIGN_'
then
    # Error raise
    echo "\e[31mERROR DETECTED\e[39m"
    echo "\e[31mSHOW LOGS integer.sequential.valid.$cDate.logs\e[39m"
else 
    echo "\e[32mOK\e[39m"
    rm $pathLogs/integer.sequential.valid.$cDate.logs
fi

echo
echo "FLOAT VALID"
echo
cd ../FLOAT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac *.deca > $pathLogs/float.sequential.valid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/float.sequential.valid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_FLOAT_ASSIGN_'
then
    # Error raise
    echo "\e[31mERROR DETECTED\e[39m"
    echo "\e[31mSHOW LOGS float.parallel.valid.$cDate.logs\e[39m"
else 
    echo "\e[32mOK\e[39m"
    rm $pathLogs/float.sequential.valid.$cDate.logs
fi


echo
echo "INTEGER INVALID"
echo
cd ../../invalid/INT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac *.deca > $pathLogs/integer.sequential.invalid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/integer.sequential.invalid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_INTEGER_ASSIGN_19.deca:10002:'
then
    echo "\e[32mOK\e[39m"
    rm $pathLogs/integer.sequential.invalid.$cDate.logs
else
    echo "\e[31mERROR DETECTED\e[39m"
    echo "\e[31mSHOW LOGS integer.sequential.invalid.$cDate.logs\e[39m"
fi


echo
echo "FLOAT INVALID"
echo
cd ../FLOAT

cDate="`date "+%Y_%m_%d_%H_%M_%S"`"
decac *.deca > $pathLogs/float.sequential.invalid.$cDate.logs 2>&1 # Redirect stdErr 2 in stdOut 1
rm *.ass

if echo $(cat $pathLogs/float.sequential.invalid.$cDate.logs ) 2>&1 | grep -q -e 'random_10000_FLOAT_ASSIGN_19.deca:10002:'
then
    echo "\e[32mOK\e[39m"
    rm $pathLogs/float.sequential.invalid.$cDate.logs
else
    echo "\e[31mERROR DETECTED\e[39m"
    echo "\e[31mSHOW LOGS float.sequential.invalid.$cDate.logs\e[39m"
fi
