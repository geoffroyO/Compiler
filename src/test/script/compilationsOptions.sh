#! /bin/sh

# Test de l'interface en ligne de commande de decac.
# On ne met ici qu'un test trivial, a vous d'en ecrire de meilleurs.

PATH=./src/main/bin:"$PATH"

decac_vide=$(decac)

if [ "$?" -ne 0 ]; then

    echo "\e[31mSans option: Erreur status different de 0\e[39m"
    exit 1
fi

if [ "$decac_vide" = "" ]; then
    echo "\e[31mSans option: Erreur pas de sortie\e[39m"
    exit 1
fi

if echo $decac_vide | grep -P -q '(?=.*?-b)(?=.*?-p)(?=.*?-v)(?=.*?-n)(?=.*?-r)(?=.*?-P)'; then
    echo "\e[32mOK \t sans option\e[39m"
else
    echo "\e[31mSans option: Erreur les options ne sont pas affichees\e[39m"
    exit 1
fi

decac_moins_b=$(decac -b)

if [ "$?" -ne 0 ]; then

    echo "\e[31m-b: Erreur status different de 0\e[39m"
    exit 1
fi

if [ "$decac_moins_b" = "" ]; then
    echo "\e[31m-b: Erreur pas de sortie\e[39m"
    exit 1
fi

if echo "$decac_moins_b" | grep -i -e "erreur" -e "error"; then
    echo "\e[31m-b: Erreur contient une erreur\e[39m"
    exit 1
fi

echo "\e[32mOK \t -b\e[39m"
