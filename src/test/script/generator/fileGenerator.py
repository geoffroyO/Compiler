import os
import sys
from utilsLexem import *


f = open("subClass_50_protected_field.deca", "w")

v = 0

f.write("// Description:\n")
f.write("//		Affiche un champ protected apres une selection de 50 et un getter\n")
f.write("// Resultat:\n")
f.write("//		5\n")

f.write("\n")

f.write("class A0 {\n")
f.write("\tprotected int x = 5;\n")
f.write("}\n")

for i in range (1, 51):
    f.write("class A" + str(i) + " extends A" + str(i-1) + "{\n")
    f.write("}\n")

f.write("{\n");

f.write("\tA50 a50 = new A50();\n")

selection = ""
for i in range (50, -1, -1):
    selection = selection + "a{}.".format(i)
selection = selection + "x"

f.write("\tprintln({});\n".format(selection))

f.write("}\n");


f.close()
