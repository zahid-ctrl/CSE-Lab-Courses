#!/usr/bin/env bash
# Runs every tests/*.zy program and compares the output with tests/*.expected.
#   ./run_tests.sh              run all tests (uses ./zyvo or ./zyvo.exe)
#   ./run_tests.sh --update     (re)create the .expected files from current output
#   ZYVO=path/to/zyvo ./run_tests.sh
# A test may have a matching .in file that is fed to the program as input.
cd "$(dirname "$0")" || exit 1
ZYVO="${ZYVO:-}"
if [ -z "$ZYVO" ]; then
    if [ -x ./zyvo ]; then ZYVO=./zyvo
    elif [ -x ./zyvo.exe ]; then ZYVO=./zyvo.exe
    else echo "Build first:  g++ -std=c++17 -Wall -Wextra src/main.cpp -o zyvo"; exit 1; fi
fi
ZYVO="$(cd "$(dirname "$ZYVO")" && pwd)/$(basename "$ZYVO")"
update=0; [ "$1" = "--update" ] && update=1
pass=0; fail=0
cd tests || exit 1
for src in *.zy; do
    name="${src%.zy}"
    input=/dev/null; [ -f "$name.in" ] && input="$name.in"
    actual="$("$ZYVO" "$src" < "$input" 2>&1; echo "[exit $?]")"
    if [ $update -eq 1 ]; then
        printf '%s\n' "$actual" > "$name.expected"; echo "updated  $name"; continue
    fi
    if [ "$actual" = "$(cat "$name.expected" 2>/dev/null)" ]; then
        echo "PASS  $name"; pass=$((pass+1))
    else
        echo "FAIL  $name"; fail=$((fail+1))
        diff <(printf '%s\n' "$actual") "$name.expected" | head -10
    fi
done
[ $update -eq 1 ] && exit 0
echo "----"; echo "$pass passed, $fail failed"
[ $fail -eq 0 ]
