#! /bin/sh

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

path=./src/test/deca/syntax/valid/created #Path execution script
cd $path || exit 1


echo "##########################"
echo "## LEXER NON REGRESSION ##"
echo "##########################"

echo 
echo "## LEXER VALID ##"
echo 

for i in *.deca
do
    if test_lex $i 2>&1 | grep -q -e "token recognition error at"
    then 
        echo "\e[31mDETECTE INVALIDE\e[39m"
        echo "\e[31mERROR IN FILE $i.deca\e[39m"
    else 
        echo "\e[32mVALID \t $i.deca\e[39m"
    fi
done

echo 
echo "## LEXER INVALID ##"
echo 

cd ../../invalid/created

for i in *.deca
do
    if test_lex $i 2>&1 | grep -q -e "token recognition error at"
    then 
        echo "\e[32mINVALID \t $i.deca\e[39m"
    else 
        echo "\e[31mDETECTE VALIDE\e[39m"
        echo "\e[31mERROR IN FILE $i.deca\e[39m"
    fi
done