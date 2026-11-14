// interpreter.hpp - PHASE 4: execution
// A tree-walking interpreter: it walks the checked AST and performs each
// command. Runtime problems (division by zero, missing input, ...) raise a
// ZyvoError with the position of the offending call.
#pragma once
#include <cmath>
#include <iostream>
#include <map>
#include <sstream>
#include <variant>
#include "ast.hpp"

namespace zyvo {

using Value = std::variant<double, std::string, bool>;

class Interpreter {
    std::map<std::string, Value> vars;
    std::istream& in;
    std::ostream& out;
    static constexpr long long MAX_LOOP = 1000000;

    [[noreturn]] void fail(const std::string& msg, const Expr& at) {
        throw ZyvoError(Phase::Runtime, msg, at.line, at.col);
    }

    // ---- value helpers ----
    static std::string show(const Value& v) {
        if (std::holds_alternative<bool>(v)) return std::get<bool>(v) ? "true" : "false";
        if (std::holds_alternative<std::string>(v)) return std::get<std::string>(v);
        double n = std::get<double>(v);
        if (std::fabs(n) < 1e15 && n == std::floor(n)) {
            std::ostringstream o; o << (long long)n; return o.str();
        }
        std::ostringstream o; o.precision(10); o << n; return o.str();
    }
    static const char* kind(const Value& v) {
        if (std::holds_alternative<bool>(v)) return "a true/false value";
        if (std::holds_alternative<std::string>(v)) return "text";
        return "a number";
    }
    double num(const Value& v, const Expr& call, size_t i) {
        if (!std::holds_alternative<double>(v))
            fail(call.spec->display + ": argument " + std::to_string(i + 1) + " must be a number but got " +
                 kind(v) + " (" + show(v) + ")", call);
        return std::get<double>(v);
    }
    bool boolean(const Value& v, const Expr& call, size_t i) {
        if (!std::holds_alternative<bool>(v))
            fail(call.spec->display + ": argument " + std::to_string(i + 1) + " must be true or false but got " +
                 kind(v) + " (" + show(v) + ")", call);
        return std::get<bool>(v);
    }
    std::string text(const Value& v, const Expr& call, size_t i) {
        if (!std::holds_alternative<std::string>(v))
            fail(call.spec->display + ": argument " + std::to_string(i + 1) + " must be text but got " +
                 kind(v) + " (" + show(v) + ")", call);
        return std::get<std::string>(v);
    }

    // UTF-8 aware split so Bangla / emoji text counts and reverses correctly.
    static std::vector<std::string> codepoints(const std::string& s) {
        std::vector<std::string> cps;
        for (size_t i = 0; i < s.size();) {
            unsigned char c = (unsigned char)s[i];
            size_t len = c < 0x80 ? 1 : (c >> 5) == 6 ? 2 : (c >> 4) == 14 ? 3 : (c >> 3) == 30 ? 4 : 1;
            cps.push_back(s.substr(i, len));
            i += len;
        }
        return cps;
    }

    // ---- statements ----
    void execBlock(const Program& b) { for (const auto& s : b) exec(*s); }

    bool condition(const Stmt& s, const char* kw) {
        Value v = eval(*s.expr);
        if (!std::holds_alternative<bool>(v))
            fail(std::string("The condition of ") + kw + " must be true or false but got " + kind(v) +
                 " (" + show(v) + ")", *s.expr);
        return std::get<bool>(v);
    }

    void exec(const Stmt& s) {
        switch (s.kind) {
            case Stmt::Call: eval(*s.expr); break;
            case Stmt::If:
                if (condition(s, "Jodi")) execBlock(s.body);
                else if (s.hasElse) execBlock(s.elseBody);
                break;
            case Stmt::While: {
                long long n = 0;
                while (condition(s, "Ghuro")) {
                    if (++n > MAX_LOOP)
                        fail("Ghuro ran more than 1000000 times - possible infinite loop (is the variable ever changed?)", *s.expr);
                    execBlock(s.body);
                }
                break;
            }
        }
    }

    // ---- expressions ----
    Value eval(const Expr& e) {
        switch (e.kind) {
            case Expr::Number: return e.num;
            case Expr::String: return e.str;
            case Expr::Bool:   return e.boolean;
            case Expr::Var:    return lookup(e.name, e);
            case Expr::Call:   return call(e);
        }
        return false;
    }

    Value lookup(const std::string& name, const Expr& at) {
        auto it = vars.find(name);
        if (it == vars.end())
            fail("Variable '" + name + "' has no value yet (it was not assigned on the path the program took)", at);
        return it->second;
    }

    Value call(const Expr& e) {
        const std::string& k = e.spec->key;
        auto V = [&](size_t i) { return eval(*e.args[i]); };
        auto N = [&](size_t i) { return num(V(i), e, i); };
        auto B = [&](size_t i) { return boolean(V(i), e, i); };
        auto S = [&](size_t i) { return text(V(i), e, i); };

        // arithmetic
        if (k == "joggo")   { double a = N(0), b = N(1); return a + b; }
        if (k == "biyano")  { double a = N(0), b = N(1); return a - b; }
        if (k == "gunon")   { double a = N(0), b = N(1); return a * b; }
        if (k == "vaggo")   { double a = N(0), b = N(1); if (b == 0) fail("Division by zero", e); return a / b; }
        if (k == "vagsesh") { double a = N(0), b = N(1); if (b == 0) fail("Modulus by zero", e); return std::fmod(a, b); }
        // comparison
        if (k == "borok")      { double a = N(0), b = N(1); return a > b; }
        if (k == "chotok")     { double a = N(0), b = N(1); return a < b; }
        if (k == "borosoman")  { double a = N(0), b = N(1); return a >= b; }
        if (k == "chotosoman") { double a = N(0), b = N(1); return a <= b; }
        if (k == "somotul")    { Value a = V(0), b = V(1); return a == b; }
        if (k == "osomot")     { Value a = V(0), b = V(1); return a != b; }
        // logic
        if (k == "ebong")  { bool a = B(0), b = B(1); return a && b; }
        if (k == "othoba") { bool a = B(0), b = B(1); return a || b; }
        if (k == "naaki")  { return !B(0); }
        // output / input / variables
        if (k == "dekhao") {
            for (size_t i = 0; i < e.args.size(); ++i) {
                if (i) out << ' ';
                out << show(V(i));
            }
            out << '\n';
            return false;
        }
        if (k == "dhoro") {
            if (e.args.size() == 2) { out << S(1); out.flush(); }
            std::string line;
            if (!std::getline(in, line)) fail("Dhoro: no input available (end of input reached)", e);
            if (!line.empty() && line.back() == '\r') line.pop_back();
            vars[e.args[0]->name] = parseInput(line);
            return false;
        }
        if (k == "mandao") { vars[e.args[0]->name] = V(1); return false; }
        if (k == "manpao") { return lookup(e.args[0]->name, *e.args[0]); }
        if (k == "baraw" || k == "komaw") {
            const Expr& a = *e.args[0];
            Value cur = lookup(a.name, a);
            if (!std::holds_alternative<double>(cur))
                fail(e.spec->display + ": variable '" + a.name + "' holds " + kind(cur) + ", not a number", a);
            vars[a.name] = std::get<double>(cur) + (k == "baraw" ? 1 : -1);
            return false;
        }
        // math helpers
        if (k == "borgo")    { double a = N(0); return a * a; }
        if (k == "ghono")    { double a = N(0); return a * a * a; }
        if (k == "borgomul") { double a = N(0); if (a < 0) fail("Square root of a negative number", e); return std::sqrt(a); }
        if (k == "porom")    { return std::fabs(N(0)); }
        // strings
        if (k == "jora")    { std::string a = show(V(0)); std::string b = show(V(1)); return a + b; }
        if (k == "doirgho") { return (double)codepoints(S(0)).size(); }
        if (k == "ulto") {
            auto cps = codepoints(S(0));
            std::string r;
            for (auto it = cps.rbegin(); it != cps.rend(); ++it) r += *it;
            return r;
        }
        if (k == "barbar") {
            std::string s = S(0);
            double c = N(1);
            if (c < 0 || c > 100000) fail("Barbar: the repeat count must be between 0 and 100000", e);
            long long n = (long long)std::llround(c);
            if ((long long)s.size() * n > 10000000) fail("Barbar: the result would be too large", e);
            std::string r;
            r.reserve(s.size() * n);
            for (long long i = 0; i < n; ++i) r += s;
            return r;
        }
        fail("Internal error: no implementation for " + e.spec->display, e);
    }

    // numbers typed by the user become numbers, everything else stays text
    static Value parseInput(const std::string& line) {
        size_t a = line.find_first_not_of(" \t");
        size_t b = line.find_last_not_of(" \t");
        if (a == std::string::npos) return line;
        std::string t = line.substr(a, b - a + 1);
        char* end = nullptr;
        double d = std::strtod(t.c_str(), &end);
        if (end && *end == '\0' && end != t.c_str() && t.find_first_of("xXnNiI") == std::string::npos) return d;
        return line;
    }

public:
    Interpreter(std::istream& input, std::ostream& output) : in(input), out(output) {}
    void run(const Program& prog) { execBlock(prog); }
};

} // namespace zyvo
