import random

LOG_PATH = "./src/test/logs/"

SUFFIX_DECA = ".deca"
SUFFIX_RES = ".deca.res"
SUFFIX_OUTPUT = ".deca.out"


TOKENS_SAME_VALUE = [
    # The value and the token are similar
    "asm",
    "class",
    "extends",
    "else",
    "false",
    "if",
    "instanceof",
    "new",
    "null",
    "readInt",
    "readFloat",
    "print",
    "println",
    "printlnx",
    "printx",
    "protected",
    "return",
    "this",
    "true",
    "while",
    "=",
    "+",
    "-",
    "*",
    "%",
    "/",
    ".",
    ",",
    ";",
    "(",
    ")",
    "{",
    "}",
    "<",
    ">",
    ">=",
    "<=",
    "==",
    "!=",
    "!",
    "&&",
    "||",
]

NO_TOKENS = [
    # No token
    " ",  # Space
    '\t',  # Tab
    '\n',  # EOL
    '\r'  # Retour chariot
]

TOKEN_SPECIFIC = [
    [0, "STRING"],
    [1, "INT"],
    [2, "FLOAT"],
    [3, "IDENT"]
]


def generateCarac():
    # Generate a single caractere
    # All the range of ASCII is possible except \n

    value = random.randint(0, 255)  # for python 2
    # value = random.randint(0, 1114111)  # for python 3

    #  https://docs.python.org/fr/3/library/functions.html#chr

    carac = chr(value)

    if carac == '"':
        carac = "\\" + '"'
    elif carac == "\\":
        carac = "\\" * 4

    return(carac)


def generateString():
    # Generate a string of random size

    size = random.randint(0, 40)

    valueString = """"""
    valueString = valueString + '"'

    for i in range(size):
        rCarac = generateCarac()
        valueString = valueString + rCarac

    valueString = valueString + '"'
    return(valueString)


def generateInteger():
    # Generate a positive number from 0 to 2^32
    return(str(random.randint(0, 2**32-1)))


def generateDec():
    lP = random.randint(0, 2**16 - 1)
    rP = random.randint(0, 2**16 - 1)

    return "{}.{}".format(lP, rP)


def generateHexaNum():
    possible = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "A", "b", "B", "c", "C", "d", "D", "e", "E", "f", "F"]

    size = random.randint(1, 40)
    res = ""
    for i in range(size):
        res = res + random.choice(possible)

    return(res)


def generateFloatDec():
    # (DEC | DEC EXP) ('F' | 'f' | );

    exp = random.choice(["e", "E", ""])
    if exp != "":
        exp = "{}{}{}".format(
            exp,
            random.choice(["+", "-", ""]),
            generateInteger())

    res = "{}{}{}".format(
        generateDec(),
        exp,
        random.choice(["f", "F", ""])
    )
    return(res)


def generateFloatHex():
    # ('0x' | 'OX') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | );

    res = "{}{}.{}{}{}{}{}".format(
        random.choice(["0x", "0X"]),
          generateHexaNum(),
          generateHexaNum(),
          random.choice(["p", "P"]),
        random.choice(["+", "-", ""]),
        generateInteger(),
        random.choice(["f", "F", ""]))
    return(res)


def generateFloat():
    val = random.randint(0, 1)
    if (val == 0):
        return(generateFloatDec())

    return(generateFloatHex())


def generateIdent():
    #  (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')* ;

    letter = [
        "A", "a",
        "B", "b",
        "C", "c",
        "D", "d",
        "E", "e",
        "F", "f",
        "G", "g",
        "H", "h",
        "I", "i",
        "J", "j",
        "K", "k",
        "L", "l",
        "M", "m",
        "N", "n",
        "O", "o",
        "P", "p",
        "Q", "q",
        "R", "r",
        "S", "s",
        "T", "t",
        "U", "u",
        "V", "v",
        "W", "w",
        "X", "x",
        "Y", "y",
        "Z", "z",
        "_", "$"
    ]

    number = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]

    res = random.choice(letter)

    size = random.randint(0, 40)
    for i in range(size):
        res = res + random.choice(letter + number)

    return(res)


def generateCommentSingleLine():
    com = """"""
    for i in range (random.randint(0, 20)):
        typeToken = random.randint(1, 3)

        if (typeToken == 1):  
            value = random.choice(TOKENS_SAME_VALUE)
            com = com + value + ' '

        if (typeToken == 2):  # No token
            value = random.choice(NO_TOKENS)
            com = com + value

        if(typeToken == 3):  # Specific command
            value = handleGeneration(random.choice(TOKEN_SPECIFIC)[0])
            com = com + value + ' '

    com = str(" ".join(com.split("\n")))

    val = """// {}""".format(com)

    return (val)

def generateCommentMultipleLine():
    com = """"""
    for i in range (random.randint(0, 50)):
        typeToken = random.randint(1, 3)

        if (typeToken == 1):  
            value = random.choice(TOKENS_SAME_VALUE)
            com = com + value + ' '

        if (typeToken == 2):  # No token
            value = random.choice(NO_TOKENS)
            com = com + value

        if(typeToken == 3):  # Specific command
            value = handleGeneration(random.choice(TOKEN_SPECIFIC)[0])
            com = com + value + ' '

    com = str(" ".join(com.split("*/")))

    val = """/* {} */""".format(com)

    return (val)

def generateInvalidToken():
    # Generate a string with no " at the end

    invalidToken = generateString()
    return(invalidToken[:len(invalidToken)-1])  # Erase the final "


def handleGeneration(value):
    if (value == 0):
        return(generateString())
    if (value == 1):
        return(generateInteger())
    if (value == 2):
        return(generateFloat())
    if (value == 3):
        return(generateIdent())


def addTokenValid(fileDeca, fileResDeca, nbTokens):
    # Complete the 2 files

    for i in range(nbTokens):
        typeToken = random.randint(1, 4)

        if (typeToken == 1):  # Token and value are similar
            value = random.choice(TOKENS_SAME_VALUE)
            fileDeca.write(value + ' ')
            fileResDeca.write(value + '\n')

        if (typeToken == 2):  # No token
            value = random.choice(NO_TOKENS)
            fileDeca.write(value)

        if(typeToken == 3):  # Specific command
            value = random.choice(TOKEN_SPECIFIC)
            fileDeca.write(handleGeneration(value[0]) + ' ')
            fileResDeca.write(value[1] + '\n')

        if(typeToken == 4): #Add comment
            typeComment = random.randint(1, 2)
            if (typeComment == 1):
                value = generateCommentSingleLine()
                fileDeca.write(value + "\n")
            if (typeComment == 2):
                value = generateCommentMultipleLine()
                fileDeca.write(value + "\n")


def addTokenInvalid(fileDeca, nbTokens):
    # Complete the 2 files

    pInvalid = random.randint(1, nbTokens)

    for i in range(nbTokens):
        typeToken = random.randint(1, 3)

        if (typeToken == 1):  # Token and value are similar
            value = random.choice(TOKENS_SAME_VALUE)
            fileDeca.write(value + ' ')

        if (typeToken == 2):  # No token
            value = random.choice(NO_TOKENS)
            fileDeca.write(value)

        if(typeToken == 3):  # Specific command
            value = random.choice(TOKEN_SPECIFIC)
            fileDeca.write(handleGeneration(value[0]) + ' ')

        if (i == pInvalid):
            value = generateInvalidToken()
            fileDeca.write(value + ' ')


def generateFilesValid(name):
    # Generate the file .deca and the res expected
    deca = open(LOG_PATH + name + SUFFIX_DECA, 'w')
    resDeca = open(LOG_PATH + name + SUFFIX_RES, 'w')

    nbTokens = random.randint(0, 2**10)

    addTokenValid(deca, resDeca, nbTokens)

    deca.close()
    resDeca.close()


def generateFileInvalid(name):
    # Generate the file .deca invalid
    deca = open(LOG_PATH + name + SUFFIX_DECA, 'w')

    nbTokens = random.randint(1, 2**10)  # Min 1 invalid token

    addTokenInvalid(deca, nbTokens)

    deca.close()


def generateValid(f, nbTokens):
    for i in range(nbTokens):
        typeToken = random.randint(1, 3)

        if (typeToken == 1):  # Token and value are similar
            value = random.choice(TOKENS_SAME_VALUE)
            f.write(value + ' ')

        if (typeToken == 2):  # No token
            value = random.choice(NO_TOKENS)
            f.write(value)

        if(typeToken == 3):  # Specific command
            value = random.choice(TOKEN_SPECIFIC)
            f.write(handleGeneration(value[0]) + ' ')

        if(typeToken == 4): #Add comment
            typeComment = random.randint(1, 2)
            if (typeComment == 1):
                value = generateCommentSingleLine()
                f.write(value + "\n")
            if (typeComment == 2):
                value = generateCommentMultipleLine()
                f.write(value + "\n")

def generateInvalid(f, nbTokens):
    # Generate the file .deca invalid
    addTokenInvalid(f, nbTokens)

