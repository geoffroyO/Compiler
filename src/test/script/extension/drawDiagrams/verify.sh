#!/bin/bash

FILE=$(basename $1 ".deca")
decac $FILE".deca"
ima $FILE.ass > $FILE".txt"
./drawDiagrams.py $FILE".txt"
