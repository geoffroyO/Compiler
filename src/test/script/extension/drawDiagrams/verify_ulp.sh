#!/bin/bash

decac sin_hex.deca
decac ulp.deca
ima sin_hex.ass > "sin_hex.txt"
ima ulp.ass > "ulp_hex.txt"
./drawDiagrams_ulp.py "sin_hex.txt" "ulp_hex.txt"

rm *.ass
rm *.txt
