lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Mots réservés

ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
FALSE: 'false';
IF: 'if';

INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';

PRINTLN: 'println';
PRINTLNX: 'printlnx';
PRINTX: 'printx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';


TRUE: 'true';
WHILE: 'while';


// Identificateurs

fragment LETTER: 'a' .. 'z' | 'A'  .. 'Z';
DIGIT: '0' .. '9';

IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')* ;

// Symboles spéciaux

EQUALS: '=';
PLUS: '+';
MINUS: '-';
MULTIPLE: '*';
PERCENT: '%';

SLASH: '/';
DOT: '.';
COMMA: ',';
SEMI: ';';


OPARENT: '(';
CPARENT: ')';

OBRACE: '{';
CBRACE: '}';


LOWER: '<';
GREATER: '>';
GREATEREQUAL: '>=';
LESSEQUAL: '<=';

EQU: '==';
NOTEQUALS: '!=';
NOT: '!';

AND: '&&';
OR: '||';

// Littéraux entiers
POSITIVE_DIGIT : '1' .. '9';
INT: '0' | POSITIVE_DIGIT DIGIT*;

// Littéraux flottants
NUM: DIGIT+;

SIGN: '+' | '-' | {}; // EMPTY ?
EXP: ('E' | 'e') SIGN NUM;

DEC: NUM '.' NUM;
FLOATDEC: (DEC | DEC EXP) ('F' | 'f' | {}); // EMPTY ?

DIGITHEX: '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
NUMHEX: DIGITHEX+;
FLOATHEX: ('0x' | 'OX') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | {});
FLOAT: FLOATDEC | FLOATHEX;

// Chaines de caracteres

fragment STRING_CAR: ~('"' | '\\' | '\n'); // Not working with the key word EOL
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

// Commentaires
COMMENT_MULTIPLE_LINES: '/*' .*? '*/' {skip();};
//COMMENT_SINGLE_LINE: '//' .* (EOL)? {skip();};


// Séparateurs
SPACE: ' ' {skip();};
TAB: '\t' {skip();};
EOL: '\n' {skip();};
RETOURCHARIOT: '\r' {skip();};


// Inclusion de fichier
FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE: '#include' (' ')* '"' FILENAME '"';

