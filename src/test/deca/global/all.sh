#!/bin/bash

FILE="example_without_objects"
echo -e "\ntest_lex -----------------------------"
test_lex $FILE".deca"
echo -e "\ntest_synt -----------------------------"
test_synt $FILE".deca"
echo -e "\ntest_context -----------------------------"
test_context $FILE".deca"
echo -e "\ndecac -----------------------------"
decac $FILE".deca"
echo -e "\ndecac -p -----------------------------"
decac -p $FILE".deca"
echo -e "\nima -----------------------------"
ima $FILE.ass