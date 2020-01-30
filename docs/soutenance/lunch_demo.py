#!/usr/bin/env python3

import sys
import os
import numpy
import math


def main():
    os.system('clear')

    quit_demo = False

    while not quit_demo:
        demo = print_and_get_input()

        if demo == 1:
            os.system("./all_stages/run.sh hello-world.deca")

        elif demo == 2:
            os.system("./all_stages/run.sh example_without_object.deca")

        elif demo == 3:
            os.system("./all_stages/run.sh example_with_object.deca")

        elif demo == 4:
            os.system("./all_stages/run.sh hello-world.deca")

        elif demo == 5:
            os.system("./exceptions/run.sh")

        elif demo == 6:
            os.system("./extension/run.sh")

        elif demo == 7:
            os.system("./all_stages/run.sh hello-world.deca")

        elif demo == 0:
            quit_demo = True
        else:
            print(" \033[1;31m Invalid input, please insert a number between 0 and 7 \033[1;0m")



def print_and_get_input():
    line = "\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581"
    line_small = "\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581\u2581"
    print("\033[1;36m" + line_small + " Projet GL 2020 | gl13 " + line_small)
    print("\n Choose the Demo:  \n")
    print(line + line_small + "\033[1;0m")
    print("\n [1] \"Hello, world\" demo\n")
    print(line + line_small)
    print("\n [2] Simple example without object\n")
    print(line + line_small)
    print("\n [3] Advanced example with object\n")
    print(line + line_small)
    print("\n [4] Example using [Math.decah]\n")
    print(line + line_small)
    print("\n [5] Exceptions\n")
    print(line + line_small)
    print("\n [6] Extension validation\n")
    print(line + line_small)
    print("\n [7] Decac Compiler options\n")
    print(line + line_small)
    print("\n\033[1;31m [0] Exit Demo \033[1;0m \n")

    demo = input("Enter your choice: ")
    loop = True
    if demo == '':
        while (loop):
            demo = input("Enter your choice: ")
            if demo != '':
                loop = False

    demo = int(demo)
    os.system('clear')
    return demo

if __name__ == '__main__':
   main()
