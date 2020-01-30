#!/bin/bash

FILE=$(basename $1 ".deca")
decac $FILE".deca"
ima $FILE.ass > $FILE".txt"
./drawDiagrams_$FILE.py $FILE".txt"

rm *.ass
rm *.txt
