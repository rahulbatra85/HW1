#!/bin/bash
set -x 
#$ java Main
#Provide 3 arguments
#	(1) <algorithm>: fast/bakery/synchronized/reentrant
#	(2) <numThread>: the number of test thread
#	(3) <numTotalInc>: the total number of increment operations performed

alg=('fast' 'bakery' 'synchronized' 'reentrant')
m=1200000
out='hw1_2.results.'`date +"%y%m%d%H%M"`


for a in "${alg[@]}"
do
    echo $a >> $out
    for n in `seq 6`
    do 
        t=`java Main $a $n $m`
        echo $n' '$t >> $out
    done
    echo >> $out
    sleep 3
done
