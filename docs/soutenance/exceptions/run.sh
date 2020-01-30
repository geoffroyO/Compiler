#!/bin/bash


cd exceptions

for file in $(find . -name "*.deca")
do
	echo -e "\033[1;34m ------------------------------------------------------------------------------- \033[1;0m "
	echo " File name is: ./exceptions"$file
	echo -e "\033[1;34m ------------------------------------------------------------------------------- \033[1;0m "
	echo -e "\n"
	cat $file
	echo -e "\n"
	read -n 1 -s -r -p " Press any key to continue"

	echo -e "\n"
	echo -e "\033[1;34m ------------------------------------------------------------------------------- \033[1;0m "
	echo -e "\n"

	echo -e "\033[1;32m  decac "$file".deca \033[1;0m  \033[1;31m \n"
	decac $file >> out
	echo -e "\033[1;0m"
	rm out
	echo -e "\n"
	read -n 1 -s -r -p " Press any key to continue"
	clear
done

read -n 1 -s -r -p " Press any key to exit"
clear
