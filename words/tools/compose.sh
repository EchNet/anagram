#!/bin/bash

INPUT=ispell-enwl-3.1.20
FILES="${INPUT}/english.* ${INPUT}/american.? ${INPUT}/altamer.?"

cat $FILES | sort | uniq > unfiltered.txt
sed -fblacklist.sed < unfiltered.txt > words.txt
