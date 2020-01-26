#! /bin/sh

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :

cd "$(dirname "$0")"/../../../../.. || exit 1

pathRootProject=$(pwd)
pathScript='/src/test/script/extension/comparaisonJavaDeca'

pathEmplacement=$pathRootProject$pathScript
cd $pathEmplacement

function=$1
value=$2

## Execute javaFile
java TargetJava $function $value