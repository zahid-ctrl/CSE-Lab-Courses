// lexer.hpp - PHASE 1: lexical analysis
// Turns the source text into a list of tokens (with line/column positions).
#pragma once
#include <string>
#include <vector>
#include <cctype>
#include <cstdlib>
#include "common.hpp"

namespace zyvo {

enum class Tok { Ident, Number, String, True, False, Else, LParen, RParen, LBrace, RBrace, Comma, End };

inline const char* tokName(Tok t) {
    switch (t) {
        case Tok::Ident:  return "IDENT";
        case Tok::Number: return "NUMBER";
        case Tok::String: return "STRING";
        case Tok::True:   return "TRUE";
        case Tok::False:  return "FALSE";
        case Tok::Else:   return "ELSE";
        case Tok::LParen: return "LPAREN";
        case Tok::RParen: return "RPAREN";
        case Tok::LBrace: return "LBRACE";
        case Tok::RBrace: return "RBRACE";
        case Tok::Comma:  return "COMMA";
        case Tok::End:    return "END";
    }
    return "?";
}

struct Token {
    Tok type;
    std::string text;   // lexeme (for strings: the decoded content)
    double num = 0;     // value for Number tokens
    int line = 1, col = 1;
};

class Lexer {
    const std::string& src;
    size_t p = 0;
    int line = 1, col = 1;

    bool atEnd() const { return p >= src.size(); }
    char peek(size_t k = 0) const { return p + k < src.size() ? src[p + k] : '\0'; }
    char advance() {
        char c = src[p++];
        if (c == '\n') { line++; col = 1; } else col++;
        return c;
    }
    [[noreturn]] void fail(const std::string& msg, int l, int c) {
        throw ZyvoError(Phase::Lexical, msg, l, c);
    }
    static bool identStart(char c) { return std::isalpha((unsigned char)c) || c == '_'; }
    static bool identPart(char c)  { return std::isalnum((unsigned char)c) || c == '_'; }

public:
    explicit Lexer(const std::string& source) : src(source) {}

    std::vector<Token> tokenize() {
        std::vector<Token> out;
        while (true) {
            skipSpaceAndComments();
            if (atEnd()) break;
            int l = line, c = col;
            char ch = peek();

            if (ch == '(') { advance(); out.push_back({Tok::LParen, "(", 0, l, c}); }
            else if (ch == ')') { advance(); out.push_back({Tok::RParen, ")", 0, l, c}); }
            else if (ch == '{') { advance(); out.push_back({Tok::LBrace, "{", 0, l, c}); }
            else if (ch == '}') { advance(); out.push_back({Tok::RBrace, "}", 0, l, c}); }
            else if (ch == ',') { advance(); out.push_back({Tok::Comma, ",", 0, l, c}); }
            else if (ch == '"') out.push_back(readString());
            else if (std::isdigit((unsigned char)ch) ||
                     (ch == '-' && std::isdigit((unsigned char)peek(1))) ||
                     (ch == '.' && std::isdigit((unsigned char)peek(1))))
                out.push_back(readNumber());
            else if (identStart(ch)) out.push_back(readWord());
            else {
                std::string shown(1, ch);
                fail("Unexpected character '" + shown + "'", l, c);
            }
        }
        out.push_back({Tok::End, "<end of file>", 0, line, col});
        return out;
    }

private:
    void skipSpaceAndComments() {
        while (!atEnd()) {
            char c = peek();
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') advance();
            else if (c == '#' || (c == '/' && peek(1) == '/')) {
                while (!atEnd() && peek() != '\n') advance();
            } else break;
        }
    }

    Token readWord() {
        int l = line, c = col;
        std::string w;
        while (!atEnd() && identPart(peek())) w += advance();
        if (w == "true")   return {Tok::True,  w, 0, l, c};
        if (w == "false")  return {Tok::False, w, 0, l, c};
        if (w == "nahole") return {Tok::Else,  w, 0, l, c};
        return {Tok::Ident, w, 0, l, c};
    }

    Token readNumber() {
        int l = line, c = col;
        std::string t;
        if (peek() == '-') t += advance();
        bool dot = false;
        while (!atEnd() && (std::isdigit((unsigned char)peek()) || peek() == '.')) {
            if (peek() == '.') {
                if (dot) fail("Malformed number: more than one decimal point", l, c);
                dot = true;
            }
            t += advance();
        }
        if (!atEnd() && identStart(peek())) {
            std::string bad = t;
            while (!atEnd() && identPart(peek())) bad += advance();
            fail("Malformed number '" + bad + "' (letters directly after digits)", l, c);
        }
        if (t.back() == '.') fail("Malformed number: '" + t + "' (digit expected after '.')", l, c);
        return {Tok::Number, t, std::strtod(t.c_str(), nullptr), l, c};
    }

    Token readString() {
        int l = line, c = col;
        advance(); // opening quote
        std::string s;
        while (true) {
            if (atEnd() || peek() == '\n') fail("Unclosed string literal (missing closing \")", l, c);
            char ch = advance();
            if (ch == '"') break;
            if (ch == '\\') {
                if (atEnd()) fail("Unclosed string literal (missing closing \")", l, c);
                char e = advance();
                switch (e) {
                    case 'n': s += '\n'; break;
                    case 't': s += '\t'; break;
                    case '"': s += '"';  break;
                    case '\\': s += '\\'; break;
                    default: fail(std::string("Unknown escape sequence '\\") + e + "'", line, col - 2);
                }
            } else s += ch;
        }
        return {Tok::String, s, 0, l, c};
    }
};

} // namespace zyvo
