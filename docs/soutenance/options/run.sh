#!/bin/bash

FILE=$(basename $1 ".deca")
cd options


echo -e "\033[1;34m ------------------------------------------------------------------------------- \033[1;0m "
echo " Decac options"
echo " File name is: "$FILE".deca"
echo -e "\033[1;34m ------------------------------------------------------------------------------- \033[1;0m "
echo -e "\n"
cat $FILE".deca"
echo -e "\n"
read -n 1 -s -r -p " Press any key to start the compiler"
clear

echo -e "\n \033[1;36m decac ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac \033[1;0m \n"
echo -e "Prints the compiler's options)"
echo -e "\n"
decac
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear

echo -e "\n \033[1;36m decac -b ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac -b \033[1;0m \n"
echo -e "Prints the team's banner (don't need any other argument)"
echo -e "\n"
decac -b
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear

echo -e "\n \033[1;36m decac -mt ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac -mt "$FILE".deca\033[1;0m \n"
echo -e "Prints methods table"
echo -e "\n"
decac -mt $FILE".deca"
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear

echo -e "\n \033[1;36m decac -p ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac -p "$FILE".deca\033[1;0m \n"
echo -e "Stops the compiler after the parser stage (stage A), and prints the program (decompilation)"
echo -e "\n"
decac -p $FILE".deca"
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear

echo -e "\n \033[1;36m decac -v ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac -v "$FILE".deca\033[1;0m \n"
echo -e "Verification without printing (except for errors)"
echo -e "\n"
decac -v $FILE".deca"
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear


echo -e "\n \033[1;36m decac -n ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac -n "$FILE".deca\033[1;0m \n"
echo -e "Compile without check the tests of execution (overflow, null pointer..)"
echo -e "\n"
decac -n $FILE".deca"
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear


echo -e "\n \033[1;36m decac -r X ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac -r 5 "$FILE".deca\033[1;0m \n"
echo -e "Limits the registers number to X registers used in the compiler (number between 4 and 16 included)."
echo -e "\n"
decac -r 5 $FILE".deca"
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear


echo -e "\n \033[1;36m decac -d ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac -d "$FILE".deca\033[1;0m \n"
echo -e "To activate debug traces, repeating the option will result in more traces levels."
echo -e "\n"
decac -d $FILE".deca"
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear


echo -e "\n \033[1;36m decac -P ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac -P "$FILE".deca prog2.deca prog3.deca \033[1;0m \n"
echo -e "To activate debug traces, repeating the option will result in more traces levels."
echo -e "\n"
rm *.ass
ls *.deca
echo -e "\n"
read -n 1 -s -r -p " Press any key to start the parallel compiling"
decac -P $FILE".deca" prog2.deca prog3.deca
echo -e "\n"
ls *.deca *.ass
echo -e "\n"
read -n 1 -s -r -p " Press any key to continue"
clear



echo -e "\n \033[1;33m [decac] ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  decac "$FILE".deca \033[1;0m"
echo -e "\n"
decac $FILE".deca"
echo -e " \033[1;32m "$FILE".ass is generated successfully! \033[1;0m \n"
read -n 1 -s -r -p " Press any key to continue"
clear

echo -e "\n \033[1;35m [ima] ----------------------------- \033[1;0m \n"
echo -e " \033[1;32m  ima "$FILE".ass \033[1;0m"
echo -e "\n"
ima $FILE.ass

echo -e "\n \033[1;0m"
read -n 1 -s -r -p " Press any key to exit"
clear
