#! /bin/sh

# All tests for cobertura

cd "$(dirname "$0")"/../../.. || exit 1
cd src/test/deca

for i in $(find . -name "*.deca")
do 
	decac $i >> out
	decac -v $i >> out
	decac -p $i >> out
	echo $i
	rm out
done


