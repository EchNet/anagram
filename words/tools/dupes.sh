#!/bin/bash
#
# Find duplicate entries, ignoring case, in the word list.
#

tr 'ABCDEFGHIJKLMNOPQRSTUVWXYZ' 'abcdefghijklmnopqrstuvwxyz' < words.txt > tmp1

sort tmp1 | uniq -D >tmp2

