import os
import sys
from utilsLexem import *


f = open("if_multiple.deca", "w")

v = 0

f.write("{\n");
f.write("\tint x = 47;\n");
f.write("\tif (x == 0) {\n");
f.write("\t\tprint(\"x = 0\");\n");
f.write("\t}\n");

for i in range(50):
    f.write("\telse if (x == {}) {} \n".format(i, '{'));
    f.write("\t\tprint(\"x = {}\");\n".format(i));
    f.write("\t}\n");

f.write("\telse {\n");
f.write("\t\tprint(\"cas par defaut\");\n");
f.write("\t}\n");
f.write("}\n");

f.close()
