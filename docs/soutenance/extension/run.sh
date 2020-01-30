#!/bin/bash


cd extension

echo -e "\n \033[1;32m [sin] ----------------------------- \033[1;0m \n"
./verify.sh sin.deca

echo -e "\n \033[1;33m [cos] ----------------------------- \033[1;0m \n"
./verify.sh cos.deca

echo -e "\n \033[1;34m [atan] ----------------------------- \033[1;0m \n"
./verify.sh atan.deca

echo -e "\n \033[1;35m [asin] ----------------------------- \033[1;0m \n"
./verify.sh asin.deca

echo -e "\n \033[1;36m [ulp] ----------------------------- \033[1;0m \n"
./verify_ulp.sh sin_hex.deca ulp.deca

read -n 1 -s -r -p " Press any key to exit"
clear
