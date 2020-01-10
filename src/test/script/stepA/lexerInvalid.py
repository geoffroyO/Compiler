import os
import sys
from utilsLexem import *


# MAIN
generateFileInvalid()

errorExpected = "token recognition error"

cmd = "test_lex {} 2>&1 | grep -q -e '{}'".format(FILE_DECA, errorExpected)
res = os.system(cmd)
# If res == 0 the token error is contain (ie the error is detected)

commandRm = "rm {}".format(FILE_DECA)

if (res == 0):
    # Error detected
    print("OK")
    os.system(commandRm)
    exit(0)

# Value not detected
# Don't delete the files that have a problem

print("KO")
exit(1)

