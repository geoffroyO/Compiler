import os
import sys
import threading
from numpy import arange

# The script need to be call in its current directory

## Class Thread


class ValueInDeca(threading.Thread):
    def __init__(self, function):
        # Init the thread
        # Give the function to test
        threading.Thread.__init__(self)
        self.function = function
        self.nameOutput = "deca_" + function + ".res"

    def generateDeca(self, value):
        # Generate a deca file for a specific value
        # Return the name of the file created

        cutValue = round(float(value), 4)
        nameFile = "{}_{}".format(self.function, cutValue)
        f = open(nameFile + ".deca", "w")

        f.write("#include \"Math.decah\"\n")
        f.write("{\n")
        if (self.function == "sqrt"):
            f.write("\tUtility m = new Utility();\n")
        else :
            f.write("\tMath m = new Math();\n")

        f.write("\tfloat b = m.{}({});\n".format(self.function, cutValue))
        f.write("\tprintx(\"{}; \", b);\n".format(cutValue))
        f.write("}\n")
        f.close()

        return(nameFile)

    def remove(self, fileName):
        os.system("rm {}.*".format(fileName))

    def getFunctionNameDeca(self):
        # Return the function call
        return(fName.get(self.function))

    def run(self):
        # Method to execute
        for i in lValues:
            # Create file Deca
            nameFile = self.generateDeca(i)

            # Compile file Deca
            os.system("decac {}.deca".format(nameFile))
            # Execute file Deca and store the value in the output file (add at the end)
            os.system("ima {}.ass >> {}".format(nameFile, self.nameOutput))

            self.remove(nameFile)


class ValueInJava(threading.Thread):
    def __init__(self, function):
        threading.Thread.__init__(self)
        self.function = function
        self.nameOutput = "java_" + function + ".res"
        self.executionName = "./executeJavaFile.sh"

    def run(self):
        f = open(self.nameOutput, "w")
        for i in lValues:
            # Execute Java
            command = "{} {} {} >> {}".format(
                self.executionName, self.function, i, self.nameOutput)
            os.system(command)
        f.close()


## Variables
# Use to generate points
startValue = -1
endValue = 1
step = 0.1

lValues = arange(startValue, endValue, step)

listFunctions = [
   "cos",
   "sin",
   # "tan", 
   "sqrt",
   #"acos",
   "asin",
   "atan"
]

#######
#######

# Compile java test
compilation = "./compilingJavaFile.sh"
os.system(compilation)


#for i in listFunctions:
#    # Run deca thread
    deca = ValueInDeca(i)
    deca.start()
#
#    # Run java thread
#    java = ValueInJava(i)
#    java.start()
