#!/bin/bash

FILE="affectation1"
echo -e "\ntest_lex -----------------------------"
test_lex $FILE".deca"
echo -e "\ntest_synt -----------------------------"
test_synt $FILE".deca"
echo -e "\ntest_context -----------------------------"
test_context $FILE".deca"
echo -e "\ndecac -----------------------------"
decac $FILE".deca"
echo -e "\nima -----------------------------"
ima $FILE.ass