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
