#!/bin/bash
###############################################################################
# Author: Mohammad Khan
# Date: 5/25/20
# Pledge: I pledge my honor that I have abided by the Stevens Honor System.
# Description: This script acts as a substitute for the rm command
#              giving the user a chance to recover files deleted accidentally
###############################################################################


readonly JUNK_FOLDER=".junk"

file=$( basename "$0" )

help=$( cat <<DELIMITER
Usage: $file [-hlp] [list of files]
   -h: Display help.
   -l: List junked files.
   -p: Purge all files.
   [list of files] with no other arguments to junk those files.
DELIMITER
)

# flags
args=0
ACTION=-1
h=0
l=0
p=0

while getopts ":hlp" option; do 
    
    case "$option" in 
        h) if [ "$h" -eq 0 ]; then  # igonre duplicate entries
                (( ++args ))
                ACTION=1;
                h=1
            fi 
            ;;
        
        l) if [ "$l" -eq 0 ]; then # igonre duplicate entries
                (( ++args ))
                ACTION=2
                l=1
            fi 
            ;;
        
        p) if [ "$p" -eq 0 ]; then # igonre duplicate entries
                (( ++args ))
                ACTION=3
                p=1
            fi 
            ;;
        
        ?)  printf "Error: Unknown option '-%s'.\n" $OPTARG >&2 # takes precedence over option count
            echo "$help"
            exit 1
            ;;
    esac

done

if [ "$args" -ge 2 ]; then
    printf "Error: Too many options enabled.\n" >&2
    echo "$help"
    exit 1
fi

# move to prase arguments
shift "$((OPTIND-1))"
list="$@"

if [ "$args" -eq 0 ] && [ -z "$list" ]; then
    echo "$help"
fi

if [ -n "$list" ] && [ "$args" -ge 1 ]; then
    printf "Error: Too many options enabled.\n" >&2
    echo "$help"
    exit 1
fi


if [ "$ACTION" -eq 1 ];then
    echo "$help"
fi 


declare -a filenames
index=0
for f in $@; do
    filenames[$index]="$f"
    (( ++index ))
done

cd ~
if [ ! -d "$JUNK_FOLDER" ];then
    mkdir -p "$JUNK_FOLDER"
fi
cd - > /dev/null

# moving files to .junk
if [ "$args" -eq 0 ] && [ -n "$list" ]; then
    for file_path in ${filenames[*]}; do 
        if [ ! -d "$file_path" ] && [ ! -f "$file_path" ];then
            echo "Warning: '$file_path' not found."
        fi

        if [ -d "$file_path" ] || [ -f "$file_path" ];then
            mv "$file_path" ~/"$JUNK_FOLDER"/ 
        fi
    done
fi 

if [ "$ACTION" -eq 2 ] && [ -z "$list" ];then
    ls -lAF ~/"$JUNK_FOLDER"/
fi 

if [ "$ACTION" -eq 3 ] && [ -z "$list" ];then
    rm -rf ~/"$JUNK_FOLDER"/*
    rm -rf ~/"$JUNK_FOLDER"/.* 1>/dev/null 2>/dev/null # to remove dotfiles
fi 

exit 0

