// parser.hpp - PHASE 2: syntax analysis (recursive descent)
//
// Grammar (EBNF):
//   program   = { statement } ;
//   statement = ifStmt | whileStmt | call ;
//   ifStmt    = "Jodi"  "(" expr ")" block [ "nahole" block ] ;
//   whileStmt = "Ghuro" "(" expr ")" block ;
//   block     = "{" { statement } "}" ;
//   call      = IDENT "(" [ expr { "," expr } ] ")" ;
//   expr      = call | NUMBER | STRING | "true" | "false" | IDENT ;
//
// Function names are case-insensitive: Joggo, joggo and JOGGO are the same.
#pragma once
#include <vector>
#include "ast.hpp"
#include "lexer.hpp"

namespace zyvo {

class Parser {
    const std::vector<Token>& toks;
    size_t pos = 0;

    const Token& cur() const { return toks[pos]; }
    const Token& next() const { return toks[pos + 1 < toks.size() ? pos + 1 : pos]; }
    bool is(Tok t) const { return cur().type == t; }
    const Token& advance() { const Token& t = toks[pos]; if (pos + 1 < toks.size()) pos++; return t; }

    [[noreturn]] void fail(const std::string& msg, const Token& at) {
        throw ZyvoError(Phase::Syntax, msg, at.line, at.col);
    }
    static std::string describe(const Token& t) {
        if (t.type == Tok::End) return "end of file";
        if (t.type == Tok::String) return "string \"" + t.text + "\"";
        return "'" + t.text + "'";
    }
    bool isKeywordCall(const char* kw) const {
        return is(Tok::Ident) && lower(cur().text) == kw && next().type == Tok::LParen;
    }

public:
    explicit Parser(const std::vector<Token>& tokens) : toks(tokens) {}

    Program parseProgram() {
        Program prog;
        while (!is(Tok::End)) prog.push_back(parseStatement());
        return prog;
    }

private:
    StmtPtr parseStatement() {
        const Token& t = cur();
        if (isKeywordCall("jodi"))  return parseIf();
        if (isKeywordCall("ghuro")) return parseWhile();

        if (t.type == Tok::Else)   fail("'nahole' has no matching Jodi(...) { ... } before it", t);
        if (t.type == Tok::RBrace) fail("Unexpected '}' (no block is open here)", t);
        if (t.type == Tok::LBrace) fail("Unexpected '{' (a block must follow Jodi(...) or Ghuro(...))", t);
        if (t.type != Tok::Ident || next().type != Tok::LParen)
            fail("A statement must be a function call like Dekhao(...), but found " + describe(t), t);

        auto s = std::make_unique<Stmt>();
        s->kind = Stmt::Call;
        s->line = t.line; s->col = t.col;
        s->expr = parseExpr();
        return s;
    }

    StmtPtr parseIf() {
        const Token& kw = advance();
        auto s = std::make_unique<Stmt>();
        s->kind = Stmt::If; s->line = kw.line; s->col = kw.col;
        s->expr = parseCondition(kw);
        s->body = parseBlock(kw);
        if (is(Tok::Else)) {
            const Token& e = advance();
            s->hasElse = true;
            s->elseBody = parseBlock(e);
        }
        return s;
    }

    StmtPtr parseWhile() {
        const Token& kw = advance();
        auto s = std::make_unique<Stmt>();
        s->kind = Stmt::While; s->line = kw.line; s->col = kw.col;
        s->expr = parseCondition(kw);
        s->body = parseBlock(kw);
        return s;
    }

    ExprPtr parseCondition(const Token& kw) {
        if (!is(Tok::LParen)) fail("Expected '(' after " + kw.text, cur());
        advance();
        if (is(Tok::RParen)) fail(kw.text + " needs a condition inside the parentheses", cur());
        ExprPtr c = parseExpr();
        if (!is(Tok::RParen)) fail("Expected ')' to close the condition of " + kw.text + ", found " + describe(cur()), cur());
        advance();
        return c;
    }

    Program parseBlock(const Token& owner) {
        if (!is(Tok::LBrace))
            fail("Expected '{' to start the block of " + owner.text + ", found " + describe(cur()), cur());
        const Token open = advance();
        Program body;
        while (!is(Tok::RBrace)) {
            if (is(Tok::End))
                fail("Missing '}' - the block opened at line " + std::to_string(open.line) +
                     ", column " + std::to_string(open.col) + " is never closed", cur());
            body.push_back(parseStatement());
        }
        advance(); // '}'
        return body;
    }

    ExprPtr parseExpr() {
        const Token& t = cur();
        auto e = std::make_unique<Expr>();
        e->line = t.line; e->col = t.col;
        switch (t.type) {
            case Tok::Number: e->kind = Expr::Number; e->num = t.num; advance(); return e;
            case Tok::String: e->kind = Expr::String; e->str = t.text; advance(); return e;
            case Tok::True:   e->kind = Expr::Bool; e->boolean = true;  advance(); return e;
            case Tok::False:  e->kind = Expr::Bool; e->boolean = false; advance(); return e;
            case Tok::Ident: {
                e->name = t.text;
                advance();
                if (!is(Tok::LParen)) { e->kind = Expr::Var; return e; }
                e->kind = Expr::Call;
                advance(); // '('
                if (is(Tok::RParen)) { advance(); return e; }
                while (true) {
                    if (is(Tok::RParen) || is(Tok::Comma))
                        fail("Expected a value before " + describe(cur()) + " in call to " + e->name, cur());
                    e->args.push_back(parseExpr());
                    if (is(Tok::Comma)) { advance(); continue; }
                    if (is(Tok::RParen)) { advance(); break; }
                    fail("Expected ',' or ')' in call to " + e->name + ", found " + describe(cur()), cur());
                }
                return e;
            }
            default:
                fail("Expected a value (number, string, true/false, variable or function call), found " + describe(t), t);
        }
    }
};

} // namespace zyvo
