#!/bin/bash

## Run all the debug test for a specific target give in parameter

FILE=$(basename $1 ".deca")

echo $FILE

read -n 1 -s -r -p " Press any key to start"
echo -e "\ntest_lex -----------------------------"
test_lex $FILE".deca"
read -n 1 -s -r -p " Press any key to continue"
echo -e "\ntest_synt -----------------------------"
test_synt $FILE".deca"
read -n 1 -s -r -p " Press any key to continue"
echo -e "\ntest_context -----------------------------"
test_context $FILE".deca"
read -n 1 -s -r -p " Press any key to continue"
echo -e "\ndecac -----------------------------"
decac $FILE".deca"
read -n 1 -s -r -p " Press any key to continue"
echo -e "\ndecac -p -----------------------------"
decac -p $FILE".deca"
read -n 1 -s -r -p " Press any key to continue"
echo -e "\nima -----------------------------"
ima $FILE.ass
read -n 1 -s -r -p " Press any key to exit"
