import os
import sys
from utilsLexem import *


f = open("AND_big_conditions.deca", "w")

v = 0

f.write("{\n");
f.write("\tboolean value;\n");

condition = ""

for i in range (50):
    condition = condition + " && true"

f.write("\tvalue = true{};\n".format(condition));


f.write("\tprint(\"true && 50 * true = \");\n");
f.write("\tif (value) {\n");
f.write("\t\tprintln(\"true\");\n");
f.write("\t}\n");
f.write("\telse {\n");
f.write("\t\tprintln(\"false\");\n");
f.write("\t}\n");

condition = ""

for i in range (50):
    condition = condition + " && false"

f.write("\tvalue = true{};\n".format(condition));


f.write("\tprint(\"true && 50 * false = \");\n");
f.write("\tif (value) {\n");
f.write("\t\tprintln(\"true\");\n");
f.write("\t}\n");
f.write("\telse {\n");
f.write("\t\tprintln(\"false\");\n");
f.write("\t}\n");


condition = ""

for i in range (50):
    condition = condition + " && true"

f.write("\tvalue = false{};\n".format(condition));


f.write("\tprint(\"false && 50 * true = \");\n");
f.write("\tif (value) {\n");
f.write("\t\tprintln(\"true\");\n");
f.write("\t}\n");
f.write("\telse {\n");
f.write("\t\tprintln(\"false\");\n");
f.write("\t}\n");


condition = ""

for i in range (50):
    condition = condition + " && false"

f.write("\tvalue = false{};\n".format(condition));


f.write("\tprint(\"false && 50 * false = \");\n");
f.write("\tif (value) {\n");
f.write("\t\tprintln(\"true\");\n");
f.write("\t}\n");
f.write("\telse {\n");
f.write("\t\tprintln(\"false\");\n");
f.write("\t}\n");

f.write("}\n");


f.close()
