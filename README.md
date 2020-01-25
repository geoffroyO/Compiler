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

### Options | TODO below ↓↓↓↓

> **$ deca -b file_name.deca**
>
> **$ deca -p file_name.deca**

### Shell programs | TODO below ↓↓↓↓

To lunch the decodor on all images in /images directoy you can use the script **run_decoder** in shell directory by typing in terminal in main directoy:

> **$ scripts/run_decoder**

This shell program will preview a progress bar for each process in addition to the time of execution and after finishing converting the jpeg image to ppm or pgm it will preview the JPEG image and the PPM or PGM image.

To lunch all included tests you can use the script **run_tests** in shell directory by typing in terminal in main directoy:

> **$ scripts/run_tests**

In this script you need press any key in order to pass to next test.

To lunch valgrind for all images in /images you can use the script **run_valgrind** in shell directory by typing in terminal in main directoy:

> **$ scripts/run_valgrind**

In this script you need press any key in order to pass to next test.

### Documentation | TODO below ↓↓↓↓

The code is fully documented and the project directory already includes a file created by **Doxygen** to generate the documentation, you can find it in the **/doc** directory.

To re-generate the documentation just open terminal at project folder root and type:
>$ TODO

### Archive contents  | TODO below ↓↓↓↓

> **/archive** contains the main executable + the tests executables.

> **/docs** contains all images in .jpg/.jpeg formats.

> **/documentation** contains project_doc.doxy file to re-generate the documentation, in addition to modules diagram image.

> **/examples** contains libraries header files.

> **/planning** contains the object files generated while compiling the projects.

> **/src** contains all the source code of the project.

> **/pom.xml** contains 4 shell scripts **run_decoder** to run the decoder on all JPEG images in /images directory, the second one **run_tests** to lunch all tests programs in terminal, the thrid one is **run_valgrind**, and the fourth script **run_decoder_without_preview**is the same as **run_decoder** but without preview for images.

> **README.md** : _current file._


### Built using
IntelliJ IDEA and Eclipse.


### Authors:
##### Lucas MORIN | Jérémy NAVARRO | Majd ODEH | Geoffroy OUDOUMANESSAH | Sylvain POUGET
Copyright © 2019 **Lucas MORIN**, **Jérémy NAVARRO**, **Majd ODEH**, **Geoffroy OUDOUMANESSAH** and **Sylvain POUGET**. All rights reserved.

École nationale supérieure d'informatique et de mathématiques appliquées
**Grenoble-INP ENSIMAG**
