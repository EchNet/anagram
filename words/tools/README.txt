Shell tools for composing a word list 

=====

Extract source files into a subfolder.
Run dos2unix over them (else grep/sed will fail on line endings).
Edit the "compose.sh" shellscript that concatenates the source files.
Update the "blacklist.sed" sed script.
Run "./filtered.sh | less" to view the blacklisted words.
