#! /bin/sh

DECAC_HOME=$("$(dirname "$0")"/../../../..)

$DECAC_HOME/src/test/script/testsCobertura/codegen.sh
$DECAC_HOME/src/test/script/testsCobertura/context.sh
$DECAC_HOME/src/test/script/testsCobertura/syntax.sh