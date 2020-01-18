#! /bin/sh

# Tous les tests sont ici

#if [ ! -r target/classpath.txt ]; then
    # Get classpath (including dependencies) from Maven
#    mvn -q -Dmdep.outputFile=target/classpath.txt dependency:build-classpath
#fi

#java -cp target/generated-classes/cobertura:target/classes:"$(cat target/classpath.txt)" \
#    fr.ensimag.deca.DecacMain "$@"

#DECAC_HOME=$(cd "$(dirname "$0")"/../../.. && pwd) 



basic-synt.sh
basic-lex.sh
basic-context.sh
basic-decac.sh
basic-gencode.sh

codegen.sh
common-tests.sh
compilationOptions.sh
context.sh
decompilation.sh
lexerInvalidRandom.sh
lexerValidRandom.sh
multi_compile.sh
syntax.sh
