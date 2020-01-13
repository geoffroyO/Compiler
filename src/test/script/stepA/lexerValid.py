import os
import sys
from utilsLexem import *

name = sys.argv[1]

# MAIN
generateFilesValid(name)

FILE_DECA = LOG_PATH + name + SUFFIX_DECA
FILE_OUTPUT = LOG_PATH + name + SUFFIX_OUTPUT
FILE_RES = LOG_PATH + name + SUFFIX_RES

commandLex = "test_lex {} > {}".format(FILE_DECA, FILE_OUTPUT)
os.system(commandLex)
# Use test_lex on the deca file. Create a new file as output

res = open(FILE_RES, 'r')
output = open(FILE_OUTPUT, 'r')

lineRes = res.readline()
lineOut = output.readline()

c = 1

while (lineRes):
    tokenExpected = lineRes.strip()
    tokenFind = lineOut.split(":")[0]

    if (tokenFind.find(tokenExpected) == -1):
        print("FAIL: LINE {}: EXPECTED {} - FIND {}".format(c,
                                                            tokenExpected, tokenFind))

        res.close()
        output.close()

        exit(1)

    c += 1
    lineRes = res.readline()
    lineOut = output.readline()


res.close()
output.close()

print("OK")
exit(0)
