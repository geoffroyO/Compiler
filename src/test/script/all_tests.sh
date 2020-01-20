#! /bin/sh

cd "$(dirname "$0")"/../../..
DECAC_HOME=$(pwd)

echo $DECAC_HOME

$DECAC_HOME/src/test/script/testsCobertura/codegen.sh
#"$DECAC_HOME"/src/test/script/testsCobertura/context.sh
#"$DECAC_HOME"/src/test/script/testsCobertura/syntax.sh
