import os
import sys
from utilsLexem import *

name = sys.argv[1]

# MAIN
generateFileInvalid(name)

FILE_DECA = LOG_PATH + name + SUFFIX_DECA


errorExpected = "token recognition error"

cmd = "test_lex {} 2>&1 | grep -q -e '{}'".format(FILE_DECA, errorExpected)
res = os.system(cmd)
# If res == 0 the token error is contain (ie the error is detected)

if (res == 0):
    # Error detected
    print("OK")
    exit(0)

# Value not detected

print("KO")
exit(1)
