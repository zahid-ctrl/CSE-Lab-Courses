#!/bin/bash

read -p "Enter directory to scan (default is current): " dir
dir=${dir:-.}

echo "Scanning directory: $dir ..."

find "$dir" -type f -exec md5sum {} + > /tmp/file_checksums.txt

awk '{print $1}' /tmp/file_checksums.txt | sort | uniq -d > /tmp/duplicate_hashes.txt

if [ ! -s /tmp/duplicate_hashes.txt ]; then
    echo "No duplicates found!"
    exit 0
fi

echo "Duplicate files found:"
while read hash; do
    grep "^$hash" /tmp/file_checksums.txt | awk '{print $2}'
    echo "----"
done < /tmp/duplicate_hashes.txt

read -p "Do you want to delete duplicates? (y/n): " choice
if [[ $choice == "y" ]]; then
    while read hash; do
        files=($(grep "^$hash" /tmp/file_checksums.txt | awk '{print $2}'))
        for ((i=1; i<${#files[@]}; i++)); do
            rm "${files[i]}"
            echo "Deleted: ${files[i]}"
        done
    done < /tmp/duplicate_hashes.txt
fi

echo "Done."