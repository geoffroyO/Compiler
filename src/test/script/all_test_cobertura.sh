#! /bin/sh

# All tests for cobertura

#PATH="./src/test/deca/"

cd "$(dirname "$0")"/../../.. || exit 1

cd ./src/test/deca/

find . -name '*.deca' -exec decac {} \;
find . -name '*.deca' -exec decac -p {} \;
find . -name '*.deca' -exec decac -v {} \;

cd ./../script

#$PATH/testsCobertura/syntax.sh
#$PATH/testsCobertura/context.sh
#$PATH/testsCobertura/codegen.sh

#$PATH/basic-context.sh
#$PATH/basic-decac.sh
#$PATH/basic-gencode.sh
#$PATH/basic-lex.sh
#$PATH/basic-synt.sh
#$PATH/common-tests.sh
#$PATH/compilationOptions.sh
#$PATH/decompilation.sh
#$PATH/lexerInvalidRandom.sh
#$PATH/lexerValidRandom.sh
#$PATH/multi_compile.sh