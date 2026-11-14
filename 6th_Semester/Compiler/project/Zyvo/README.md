# Zyvo Programming Language

Zyvo is a small custom programming language built for the **Compiler Lab** course.
Instead of conventional keywords it uses its own vocabulary (`Joggo` = add,
`Biyano` = subtract, `Dekhao` = print, ...). The project contains a C++ interpreter
that shows the four classic phases of a language processor:

`source code -> Lexer -> Parser (syntax tree) -> Semantic analyser -> Interpreter`

## Build and run

Needs GCC / g++ with C++17 (for example MSYS2 UCRT64 on Windows).

```bash
g++ -std=c++17 -Wall -Wextra src/main.cpp -o zyvo        # on Windows the output is zyvo.exe
./zyvo examples/01_proposal_example.zy                   # prints 15
```

| Command | What it does |
|---|---|
| `zyvo prog.zy` | run a program |
| `zyvo prog.zy --tokens` | also print the token list (lexical analysis) |
| `zyvo prog.zy --ast` | also print the syntax tree (syntax analysis) |
| `zyvo prog.zy --check` | check only, do not run (semantic analysis) |
| `zyvo --table` / `--table-md` | print the 30-function table |

Run all tests: `bash run_tests.sh`

## The language in one minute

```
manDao(a, 10)                 # variables: no quotes, created by ManDao
manDao(b, 5)
manDao(result, joggo(a, b))   # calls can be nested
dekhao(result)                # prints 15

jodi(borok(a, b)) {           # if / else
    dekhao("a is bigger")
} nahole {
    dekhao("b is bigger")
}

manDao(i, 1)
ghuro(chotoSoman(i, 3)) {     # loop
    dekhao(i)
    baraw(i)                  # i = i + 1
}
```

* One statement per call; statements are separated by whitespace or new lines.
* Function names are **not** case-sensitive (`Joggo`, `joggo`, `JOGGO` are the same).
* Values: numbers (`10`, `-3`, `2.5`), text (`"Hello"` with `\n \t \" \\`), `true`, `false`.
* Comments start with `#` or `//`.
* `nahole` is the only extra word: it introduces the else-block of `Jodi`.

## Function table (30 commands)

| # | Zyvo | Standard operation | Purpose | Example |
|---|------|--------------------|---------|---------|
| 1 | `Joggo` | Add / Sum | Add two numbers | `Joggo(4, 2)  -> 6` |
| 2 | `Biyano` | Subtract | Subtract two numbers | `Biyano(4, 2) -> 2` |
| 3 | `Gunon` | Multiply | Multiply two numbers | `Gunon(4, 2)  -> 8` |
| 4 | `Vaggo` | Divide | Divide two numbers | `Vaggo(9, 2)  -> 4.5` |
| 5 | `Vagsesh` | Modulus | Find the remainder | `Vagsesh(9, 2) -> 1` |
| 6 | `Borok` | Greater Than | Is the first number greater? | `Borok(4, 2)       -> true` |
| 7 | `Chotok` | Less Than | Is the first number smaller? | `Chotok(4, 2)      -> false` |
| 8 | `Somotul` | Equal | Check equality of two values | `Somotul(3, 3)     -> true` |
| 9 | `Osomot` | Not Equal | Check inequality of two values | `Osomot(3, 4)      -> true` |
| 10 | `BoroSoman` | Greater Than or Equal | Greater than or equal? | `BoroSoman(4, 4)   -> true` |
| 11 | `ChotoSoman` | Less Than or Equal | Less than or equal? | `ChotoSoman(3, 4)  -> true` |
| 12 | `Ebong` | Logical AND | True if both conditions are true | `Ebong(true, false)  -> false` |
| 13 | `Othoba` | Logical OR | True if at least one condition is true | `Othoba(true, false) -> true` |
| 14 | `Naaki` | Logical NOT | Reverse a logical value | `Naaki(true)         -> false` |
| 15 | `Dekhao` | Print / Output | Display one or more values | `Dekhao("Sum =", x)` |
| 16 | `Dhoro` | Input | Read a line from the user into a variable | `Dhoro(age, "Age? ")` |
| 17 | `ManDao` | Assign | Assign a value to a variable | `ManDao(x, 10)` |
| 18 | `ManPao` | Get Variable | Retrieve a variable's value | `ManPao(x)  -> 10` |
| 19 | `Baraw` | Increment | Increase a numeric variable by one | `Baraw(x)   (10 -> 11)` |
| 20 | `Komaw` | Decrement | Decrease a numeric variable by one | `Komaw(x)   (11 -> 10)` |
| 21 | `Borgo` | Square | Calculate the square of a number | `Borgo(5)     -> 25` |
| 22 | `Ghono` | Cube | Calculate the cube of a number | `Ghono(3)     -> 27` |
| 23 | `Borgomul` | Square Root | Find the square root | `Borgomul(81) -> 9` |
| 24 | `Porom` | Absolute Value | Return the absolute value | `Porom(-7)    -> 7` |
| 25 | `Jora` | String Concatenation | Join two values into text | `Jora("Zy", "vo") -> Zyvo` |
| 26 | `Doirgho` | String Length | Count characters in a string | `Doirgho("Zyvo")   -> 4` |
| 27 | `Ulto` | Reverse String | Reverse a string | `Ulto("abc")       -> cba` |
| 28 | `Barbar` | Repeat String | Repeat a string several times | `Barbar("Hi", 3)   -> HiHiHi` |
| 29 | `Jodi` | Conditional Statement | Run a block only if a condition is true (optional 'nahole' else-block) | `Jodi(Borok(a, b)) { ... } nahole { ... }` |
| 30 | `Ghuro` | Loop | Repeat a block while a condition is true | `Ghuro(Chotok(i, 5)) { ... }` |

Full design description, grammar and sample runs: see [docs/REPORT.md](docs/REPORT.md).

## Folder layout

```
src/        lexer.hpp  parser.hpp  ast.hpp  semantic.hpp  interpreter.hpp
            functions.hpp (the function table)  common.hpp  main.cpp
examples/   8 sample Zyvo programs
tests/      26 test programs with expected output (valid + error cases)
docs/       REPORT.md
run_tests.sh
```
