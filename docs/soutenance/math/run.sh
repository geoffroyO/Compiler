#!/bin/bash

FILE=$(basename $1 ".deca")
cd math


echo -e "\033[1;34m ------------------------------------------------------------------------------- \033[1;0m "
echo " Program using the Math.decah library"
echo " File name is: "$FILE".deca"
echo -e "\033[1;34m ------------------------------------------------------------------------------- \033[1;0m "
echo -e "\n"
cat $FILE".deca"
echo -e "\n"
read -n 1 -s -r -p " Press any key to start the compiler"
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
