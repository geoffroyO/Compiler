echo "## TEST LEX"
test_lex $1
echo "## TEST SYN"
test_synt $1
echo "## TEST CON"
test_context $1
echo "## TEST DECOM"
decac -p $1
echo "## TEST EXE"
decac $1 



if [ "$#" -eq 1 ]
then 
    # only 1 argument
    # display
    ima ${1%.*}.ass 
else
    ima ${1%.*}.ass > res/$1.res
fi

rm ${1%.*}.ass
