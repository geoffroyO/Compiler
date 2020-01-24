#! /bin/sh

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :

cd "$(dirname "$0")"/../../../../.. || exit 1

pathRootProject=$(pwd)
pathScript='/src/test/script/extension'

pathEmplacement=$pathRootProject$pathScript
cd $pathEmplacement

## Create the .class file
javac TargetJava.java
