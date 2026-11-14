// ast.hpp - the abstract syntax tree built by the parser
#pragma once
#include <memory>
#include <string>
#include <vector>
#include <ostream>
#include "common.hpp"
#include "functions.hpp"

namespace zyvo {

struct Expr;
struct Stmt;
using ExprPtr = std::unique_ptr<Expr>;
using StmtPtr = std::unique_ptr<Stmt>;

struct Expr {
    enum Kind { Number, String, Bool, Var, Call } kind;
    double num = 0;
    std::string str;            // string literal value
    bool boolean = false;
    std::string name;           // variable name or function name as written
    std::vector<ExprPtr> args;  // call arguments
    int line = 1, col = 1;
    const FuncSpec* spec = nullptr;  // filled in by the semantic analyser
    Type type = Type::Any;           // static type, filled in by the semantic analyser
};

struct Stmt {
    enum Kind { Call, If, While } kind;
    ExprPtr expr;                    // call expression, or the condition
    std::vector<StmtPtr> body;       // block (If: then-block, While: loop body)
    std::vector<StmtPtr> elseBody;   // only for If
    bool hasElse = false;
    int line = 1, col = 1;
};

using Program = std::vector<StmtPtr>;

// ---- pretty printer for "--ast" ----
inline std::string escapeForDump(const std::string& s) {
    std::string o;
    for (char c : s) {
        if (c == '\n') o += "\\n";
        else if (c == '\t') o += "\\t";
        else o += c;
    }
    return o;
}

inline void dumpExpr(const Expr& e, std::ostream& os, int ind) {
    std::string pad(ind * 2, ' ');
    switch (e.kind) {
        case Expr::Number: {
            os << pad << "Number " << e.num << "\n"; break;
        }
        case Expr::String: os << pad << "String \"" << escapeForDump(e.str) << "\"\n"; break;
        case Expr::Bool:   os << pad << "Bool " << (e.boolean ? "true" : "false") << "\n"; break;
        case Expr::Var:    os << pad << "Variable " << e.name << "\n"; break;
        case Expr::Call:
            os << pad << "Call " << e.name << "\n";
            for (const auto& a : e.args) dumpExpr(*a, os, ind + 1);
            break;
    }
}

inline void dumpStmts(const Program& ss, std::ostream& os, int ind);

inline void dumpStmt(const Stmt& s, std::ostream& os, int ind) {
    std::string pad(ind * 2, ' ');
    switch (s.kind) {
        case Stmt::Call:
            dumpExpr(*s.expr, os, ind);
            break;
        case Stmt::If:
            os << pad << "If (Jodi)\n" << pad << "  condition:\n";
            dumpExpr(*s.expr, os, ind + 2);
            os << pad << "  then:\n";
            dumpStmts(s.body, os, ind + 2);
            if (s.hasElse) {
                os << pad << "  else (nahole):\n";
                dumpStmts(s.elseBody, os, ind + 2);
            }
            break;
        case Stmt::While:
            os << pad << "While (Ghuro)\n" << pad << "  condition:\n";
            dumpExpr(*s.expr, os, ind + 2);
            os << pad << "  body:\n";
            dumpStmts(s.body, os, ind + 2);
            break;
    }
}

inline void dumpStmts(const Program& ss, std::ostream& os, int ind) {
    for (const auto& s : ss) dumpStmt(*s, os, ind);
}

} // namespace zyvo
