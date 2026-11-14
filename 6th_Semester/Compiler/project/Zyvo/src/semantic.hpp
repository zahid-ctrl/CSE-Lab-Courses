// semantic.hpp - PHASE 3: semantic analysis
// Checks, WITHOUT running the program:
//   * every function exists (with a "Did you mean ...?" hint)
//   * the number of arguments is right
//   * arguments have the right type (number / text / true-false)
//   * variables are defined before they are used
//   * conditions of Jodi / Ghuro are true/false values
//   * obvious mistakes such as dividing by the literal 0
// It also annotates the AST (function spec + static type of every expression)
// so that the interpreter does not have to look anything up again.
#pragma once
#include <map>
#include <vector>
#include "ast.hpp"

namespace zyvo {

struct Diag {
    std::string msg;
    int line, col;
};

class SemanticAnalyzer {
    std::map<std::string, Type> vars;   // variable -> static type (Any if unknown)
    std::vector<Diag> diags;

    void err(const std::string& msg, int line, int col) { diags.push_back({msg, line, col}); }

    static std::string ordinal(size_t i) { return "argument " + std::to_string(i + 1); }

    static bool compatible(Type want, Type got) {
        return want == Type::Any || got == Type::Any || want == got;
    }

    void checkBlock(Program& block) { for (auto& s : block) checkStmt(*s); }

    void checkStmt(Stmt& s) {
        switch (s.kind) {
            case Stmt::Call:
                checkExpr(*s.expr);
                break;
            case Stmt::If:
            case Stmt::While: {
                const char* kw = s.kind == Stmt::If ? "Jodi" : "Ghuro";
                Type t = checkExpr(*s.expr);
                if (t != Type::Bool && t != Type::Any)
                    err(std::string("The condition of ") + kw + " must be a true/false value, but this is " +
                        (t == Type::Void ? "a command that returns nothing" : std::string("a ") + typeName(t)),
                        s.expr->line, s.expr->col);
                checkBlock(s.body);
                if (s.hasElse) checkBlock(s.elseBody);
                break;
            }
        }
    }

    Type checkExpr(Expr& e) {
        switch (e.kind) {
            case Expr::Number: return e.type = Type::Num;
            case Expr::String: return e.type = Type::Str;
            case Expr::Bool:   return e.type = Type::Bool;
            case Expr::Var: {
                auto it = vars.find(e.name);
                if (it == vars.end()) {
                    err("Variable '" + e.name + "' is used before it is defined (define it with ManDao(" + e.name + ", value))",
                        e.line, e.col);
                    return e.type = Type::Any;
                }
                return e.type = it->second;
            }
            case Expr::Call: return checkCall(e);
        }
        return Type::Any;
    }

    Type checkCall(Expr& e) {
        const FuncSpec* f = findFunction(e.name);
        if (!f) {
            std::string msg = "Unknown function '" + e.name + "'";
            std::string hint = suggestFunction(e.name);
            if (!hint.empty()) msg += " - did you mean " + hint + "?";
            err(msg, e.line, e.col);
            return e.type = Type::Any;
        }
        e.spec = f;
        if (f->control) {
            err(f->display + " is a block statement and cannot be used as a value. Write: " + f->display +
                "(condition) { ... }", e.line, e.col);
            return e.type = Type::Any;
        }

        const size_t n = e.args.size();
        const size_t maxArgs = f->params.size();
        if (n < f->minArgs || (!f->variadic && n > maxArgs)) {
            std::string expect;
            if (f->variadic) expect = "at least " + std::to_string(f->minArgs);
            else if (f->minArgs == maxArgs) expect = std::to_string(maxArgs);
            else expect = std::to_string(f->minArgs) + " to " + std::to_string(maxArgs);
            err(f->display + " expects " + expect + " argument(s) but got " + std::to_string(n), e.line, e.col);
            return e.type = f->ret;
        }

        std::string declareName;   // variable defined by this call (ManDao / Dhoro)
        Type declareType = Type::Any;

        for (size_t i = 0; i < n; ++i) {
            Expr& a = *e.args[i];
            const ParamSpec& ps = f->params[std::min(i, maxArgs - 1)];

            if (ps.kind == ParamKind::Name) {
                if (a.kind != Expr::Var) {
                    err(f->display + ": " + ordinal(i) + " must be a plain variable name like x, not a value",
                        a.line, a.col);
                    continue;
                }
                if (f->key == "mandao" || f->key == "dhoro") {
                    declareName = a.name;
                } else {
                    auto it = vars.find(a.name);
                    if (it == vars.end()) {
                        err("Variable '" + a.name + "' is used before it is defined", a.line, a.col);
                    } else if ((f->key == "baraw" || f->key == "komaw") &&
                               it->second != Type::Num && it->second != Type::Any) {
                        err(f->display + " needs a number variable, but '" + a.name + "' holds " +
                            std::string(typeName(it->second)), a.line, a.col);
                    }
                    if (f->key == "manpao" && it != vars.end()) e.type = it->second;
                }
                continue;
            }

            Type t = checkExpr(a);
            if (t == Type::Void) {
                err(f->display + ": " + ordinal(i) + " has no value (the command inside returns nothing)", a.line, a.col);
                continue;
            }
            if (!compatible(ps.type, t))
                err(f->display + ": " + ordinal(i) + " must be a " + typeName(ps.type) + ", but got a " + typeName(t),
                    a.line, a.col);

            if (f->key == "mandao" && i == 1) declareType = t;

            // simple constant checks
            if ((f->key == "vaggo" || f->key == "vagsesh") && i == 1 && a.kind == Expr::Number && a.num == 0)
                err(f->display + ": the second argument is 0 (" + (f->key == "vaggo" ? "division" : "modulus") + " by zero)",
                    a.line, a.col);
            if (f->key == "borgomul" && a.kind == Expr::Number && a.num < 0)
                err("Borgomul: square root of a negative number", a.line, a.col);
            if (f->key == "barbar" && i == 1 && a.kind == Expr::Number && a.num < 0)
                err("Barbar: the repeat count cannot be negative", a.line, a.col);
        }

        if (!declareName.empty()) {
            auto it = vars.find(declareName);
            if (it == vars.end()) vars[declareName] = declareType;
            else if (it->second != declareType) it->second = Type::Any;
        }

        if (f->key != "manpao") e.type = f->ret;
        return e.type;
    }

public:
    std::vector<Diag> analyze(Program& prog) {
        checkBlock(prog);
        return diags;
    }
};

} // namespace zyvo
