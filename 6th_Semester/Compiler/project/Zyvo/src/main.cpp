// Zyvo - a small custom programming language (Compiler Lab project)
//
// Pipeline:  source text -> Lexer -> tokens -> Parser -> AST
//            -> Semantic analyser -> checked AST -> Interpreter -> output
//
// Build:  g++ -std=c++17 -Wall -Wextra src/main.cpp -o zyvo
// Usage:  zyvo program.zy [--tokens] [--ast] [--check]
//         zyvo --table | --table-md | --help
#include <fstream>
#include <iomanip>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>

#include "common.hpp"
#include "functions.hpp"
#include "lexer.hpp"
#include "parser.hpp"
#include "semantic.hpp"
#include "interpreter.hpp"

using namespace zyvo;

static void usage() {
    std::cout <<
        "Zyvo - a small custom programming language\n"
        "\n"
        "Usage:\n"
        "  zyvo <program.zy>            run a Zyvo program\n"
        "  zyvo <program.zy> --tokens   also show the token list      (lexical analysis)\n"
        "  zyvo <program.zy> --ast      also show the syntax tree     (syntax analysis)\n"
        "  zyvo <program.zy> --check    check only, do not run        (semantic analysis)\n"
        "  zyvo --table                 print the function table\n"
        "  zyvo --table-md              print the function table as a Markdown table\n"
        "  zyvo --help                  show this help\n"
        "\n"
        "Exit codes: 0 ok, 1 usage/file error, 2 lexical or syntax error,\n"
        "            3 semantic error, 4 runtime error\n";
}

static std::string sourceLine(const std::string& src, int lineNo) {
    std::istringstream in(src);
    std::string line;
    for (int i = 1; std::getline(in, line); ++i)
        if (i == lineNo) {
            if (!line.empty() && line.back() == '\r') line.pop_back();
            return line;
        }
    return "";
}

static void report(const std::string& file, const std::string& src, const char* kind,
                   const std::string& msg, int line, int col) {
    std::cerr << file << ":" << line << ":" << col << ": " << kind << ": " << msg << "\n";
    std::string text = sourceLine(src, line);
    if (!text.empty()) {
        std::string num = std::to_string(line);
        std::cerr << "  " << num << " | " << text << "\n";
        std::string pad;
        for (int i = 0; i < col - 1 && i < (int)text.size(); ++i) pad += (text[i] == '\t' ? '\t' : ' ');
        std::cerr << "  " << std::string(num.size(), ' ') << " | " << pad << "^\n";
    }
}

static void printTokens(const std::vector<Token>& toks) {
    std::cout << "=== TOKENS (lexical analysis) ===\n";
    std::cout << std::left << std::setw(9) << "LINE:COL" << std::setw(8) << "TYPE" << "TEXT\n";
    for (const auto& t : toks) {
        std::string pos = std::to_string(t.line) + ":" + std::to_string(t.col);
        std::string text = t.type == Tok::String ? "\"" + escapeForDump(t.text) + "\"" : t.text;
        std::cout << std::left << std::setw(9) << pos << std::setw(8) << tokName(t.type) << text << "\n";
    }
    std::cout << "\n";
}

static void printTable() {
    std::cout << "=== Zyvo function table (" << functionTable().size() << " commands) ===\n";
    std::cout << std::left << std::setw(4) << "No." << std::setw(12) << "Zyvo"
              << std::setw(24) << "Standard operation" << "Purpose\n";
    std::cout << std::string(78, '-') << "\n";
    int i = 1;
    for (const auto& f : functionTable())
        std::cout << std::left << std::setw(4) << i++ << std::setw(12) << f.display
                  << std::setw(24) << f.standard << f.purpose << "\n"
                  << std::setw(40) << "" << "e.g. " << f.example << "\n";
}

static void printTableMd() {
    std::cout << "| # | Zyvo | Standard operation | Purpose | Example |\n|---|------|--------------------|---------|---------|\n";
    int i = 1;
    for (const auto& f : functionTable())
        std::cout << "| " << i++ << " | `" << f.display << "` | " << f.standard << " | " << f.purpose
                  << " | `" << f.example << "` |\n";
}

int main(int argc, char* argv[]) {
    std::string file;
    bool showTokens = false, showAst = false, checkOnly = false;
    for (int i = 1; i < argc; ++i) {
        std::string a = argv[i];
        if (a == "--help" || a == "-h") { usage(); return 0; }
        else if (a == "--table")    { printTable(); return 0; }
        else if (a == "--table-md") { printTableMd(); return 0; }
        else if (a == "--tokens") showTokens = true;
        else if (a == "--ast")    showAst = true;
        else if (a == "--check")  checkOnly = true;
        else if (a.rfind("--", 0) == 0) { std::cerr << "Unknown option: " << a << "\n\n"; usage(); return 1; }
        else if (file.empty()) file = a;
        else { std::cerr << "Only one source file can be given.\n"; return 1; }
    }
    if (file.empty()) { usage(); return 1; }

    std::ifstream f(file, std::ios::binary);
    if (!f) { std::cerr << "Error: could not open file '" << file << "'\n"; return 1; }
    std::stringstream buf;
    buf << f.rdbuf();
    std::string src = buf.str();

    std::vector<Token> tokens;
    Program program;

    // ---- Phase 1 + 2: lexical and syntax analysis ----
    try {
        tokens = Lexer(src).tokenize();
        if (showTokens) printTokens(tokens);
        program = Parser(tokens).parseProgram();
    } catch (const ZyvoError& e) {
        report(file, src, phaseName(e.phase), e.what(), e.line, e.col);
        return 2;
    }

    // ---- Phase 3: semantic analysis ----
    auto diags = SemanticAnalyzer().analyze(program);
    if (showAst) {
        std::cout << "=== SYNTAX TREE (syntax analysis) ===\n";
        dumpStmts(program, std::cout, 0);
        std::cout << "\n";
    }
    if (!diags.empty()) {
        for (const auto& d : diags) report(file, src, phaseName(Phase::Semantic), d.msg, d.line, d.col);
        std::cerr << diags.size() << " semantic error(s) found - program not run.\n";
        return 3;
    }
    if (checkOnly) {
        std::cout << "OK: no lexical, syntax or semantic errors found.\n";
        return 0;
    }

    // ---- Phase 4: execution ----
    if (showTokens || showAst) std::cout << "=== OUTPUT ===\n";
    try {
        Interpreter(std::cin, std::cout).run(program);
    } catch (const ZyvoError& e) {
        std::cout.flush();
        report(file, src, phaseName(e.phase), e.what(), e.line, e.col);
        return 4;
    }
    return 0;
}
