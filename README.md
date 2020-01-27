# Projet Génie Logiciel, Ensimag.
Equipe gl13, le 01/01/2020.


**Decac** is a compiler for "Deca" language, developed under CentOS, macOS and Ubuntu.

### Compiling the project using "MAVEN"

You can compile the project by typing in terminal:

> $ mvn compile

You can clean the project by typing in terminal:

> $ mvn clean


### Usage

To run the compiler and generate the assembly file you simply need to open a terminal in any directory you want where the ".deca" files and then type:

> **$ deca file_name.deca**

Then to execute the assembly file type:

> **$ ima file_name.ass**

### Options for Decac

[-b] To print the team's banner (can't get arguments other than -b)
> **$ deca -b**

[-mt] To print the methods table of deca program
> **$ deca -mt file_name.deca**

[-p] To stop the compiler after the parser stage (stage A).
> **$ deca -p file_name.deca**

[-v] To stop the compiler after the syntax analysis stage (stage B).
> **$ deca -v file_name.deca**

[-n] Compile without check the tests of execution (overflow, null pointer..)
> **$ deca -n file_name.deca**

[-r X] Limits the registers number to X registers used in the compiler (number between 4 and 16 included).
> **$ deca -r X file_name.deca**

[-d] To activate debug traces, repeating the option will result in more traces levels.
> **$ deca -d file_name.deca**

[-P] To execute multiple .deca files at once (parallel).
> **$ deca -P file1.deca file2.deca**


### Built using
IntelliJ IDEA and Eclipse.


### Authors:
##### Lucas MORIN | Jérémy NAVARRO | Majd ODEH | Geoffroy OUDOUMANESSAH | Sylvain POUGET
Copyright © 2019 **Lucas MORIN**, **Jérémy NAVARRO**, **Majd ODEH**, **Geoffroy OUDOUMANESSAH** and **Sylvain POUGET**. All rights reserved.

École nationale supérieure d'informatique et de mathématiques appliquées
**Grenoble-INP ENSIMAG**
