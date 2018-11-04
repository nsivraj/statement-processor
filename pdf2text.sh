#!/bin/sh
#./pdf2text.sh ~/Documents/uccu.com/uccu_statements/2018/uccu.statement.jan.2018.pdf > ~/forge/tools/textricator-9.0.44/uccu_statements/2018/uccu.statement.jan.2018.textricator.csv
./textricator text --input-format=pdf.pdfbox $1
