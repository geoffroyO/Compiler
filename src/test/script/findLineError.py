import os
import sys


# Convention
# <Comment> Ligne <nbLignes> : ...

fileName = sys.argv[1]
tokenExpected= "Ligne"
res = ""

fichier = open(fileName, "r")

for i in fichier:
    find = i.find(tokenExpected) 

    if i.find(tokenExpected) != -1:

        current = find + len(tokenExpected)

        while (i[current] != ':'):
            res = res + i[current]
            current += 1
            
        print(int(res))
        quit()

print("")
