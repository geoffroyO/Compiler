#! /bin/sh

## Verifie la gestion des options du compilateur


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

# Test de l'interface en ligne de commande de decac.
# On ne met ici qu'un test trivial, a vous d'en ecrire de meilleurs.

PATH=./src/main/bin:"$PATH"

## Sans option
decac_vide=$(decac)

if [ "$?" -ne 0 ]; then
    pFailure "Sans option: Erreur status different de 0"
    exit 1
fi

if [ "$decac_vide" = "" ]; then
    pFailure "Sans option: Erreur pas de sortie"
    exit 1
fi

if echo $decac_vide | grep -P -q '(?=.*?-b)(?=.*?-p)(?=.*?-v)(?=.*?-n)(?=.*?-r)(?=.*?-P)'; then

    pSuccess "OK \t sans option"
else
    pFailure "Sans option: Erreur les options ne sont pas affichees"
    exit 1
fi


## Option -b
decac_moins_b=$(decac -b)

if [ "$?" -ne 0 ]; then

    pFailure "-b: Erreur status different de 0"
    exit 1
fi

if [ "$decac_moins_b" = "" ]; then
    pFailure "-b: Erreur pas de sortie"
    exit 1
fi

if echo "$decac_moins_b" | grep -i -e "erreur" -e "error"; then
    pFailure "-b: Erreur contient une erreur"
    exit 1
fi

pSuccess "OK \t -b"

## Option -p 
# Option teste avec decompilation

## Option -v
# Option teste avec contexte

## Option -n
printf "{ float x = 1/0; }" > test.deca
decac -n test.deca
ima test.ass > output.res 2>&1

if (grep -e "divide" -e "division" output.res); then
    # We find a register use with value greater or equal to 10
    pFailure "-n: n'a pas le comportement escompte"
    exit 1
else 
    pSuccess "OK \t -n"
fi
rm output.res test.ass test.deca


## Option -r X

## Generate file deca
printf "{ \n \tint a = 5;\n \ta = " > test.deca
for i in `seq 1 50`;
do
    printf " a + " >> test.deca
    #echo "a = a + 5;" >> test.deca
done
printf " 0;\n}" >> test.deca

decac -r 10 test.deca


if (grep -w "R1[0-9]" test.ass); then
    # We find a register use with value greater or equal to 10
    pFailure "-r: n'a pas le comportement escompte"
    exit 1
else 
    pSuccess "OK \t -r"
fi
rm test.deca test.ass



## Option -d
echo "{int a = 5;}" > test.deca
decac -d test.deca > output.res

if (grep -i -q "INFO" output.res); then
    pSuccess "OK \t -d"
else 
    pFailure "-d: n'a pas le comportement escompte"
    exit 1
fi

rm test.deca output.res test.ass

## Option -P 
# Option teste avec multi_compile




