import os, sys
from utilsLexem import *

# MAIN
generateFilesValid()


commandLex = "test_lex {} > {}".format(FILE_DECA, FILE_OUTPUT)
os.system(commandLex)
# Use test_lex on the deca file. Create a new file as output

commandRm = "rm {} {} {}".format(FILE_DECA, FILE_OUTPUT, FILE_RES)

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

        # os.system(commandRm)
        # Don't delete the files that have a problem
        exit(1)

    c += 1
    lineRes = res.readline()
    lineOut = output.readline()


res.close()
output.close()

os.system(commandRm)
print("OK")
exit(0)