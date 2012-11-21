#!/bin/bash
#
# Find the potential roman numerals in the word list.
#

cat -n words.txt > tmp1
java -jar c:/temp/build/roman/roman*.jar words.txt | cat -n > tmp2
join tmp1 tmp2 | grep -v -e -1 | awk '{print $2}'

