#!/bin/bash

diff unfiltered.txt words.txt | grep '^<' | awk '{print $2}' | sort
