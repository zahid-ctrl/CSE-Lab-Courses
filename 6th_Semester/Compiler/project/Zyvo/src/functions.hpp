// functions.hpp - the Zyvo function table (30 commands)
// Every built-in command is described once here. The semantic analyser uses
// the signatures, the interpreter uses the key, and "--table" prints it.
#pragma once
#include <string>
#include <vector>
#include <algorithm>
#include <cctype>
#include "common.hpp"

namespace zyvo {

// A parameter is either a normal value (evaluated) or a bare variable name.
enum class ParamKind { Value, Name };

struct ParamSpec {
    ParamKind kind;
    Type type;
};

struct FuncSpec {
    std::string display;    // name as written in the proposal, e.g. "BoroSoman"
    std::string key;        // lower-case lookup key, e.g. "borosoman"
    std::string standard;   // standard operation it replaces
    std::string purpose;    // short description
    std::string example;    // example call
    std::vector<ParamSpec> params;
    size_t minArgs;         // minimum number of arguments
    bool variadic;          // true: extra arguments allowed (same type as last)
    Type ret;               // return type
    bool control = false;   // true for Jodi / Ghuro (block statements)
};

inline std::string lower(std::string s) {
    std::transform(s.begin(), s.end(), s.begin(),
                   [](unsigned char c) { return (char)std::tolower(c); });
    return s;
}

inline const std::vector<FuncSpec>& functionTable() {
    using PK = ParamKind;
    const ParamSpec N{PK::Value, Type::Num};
    const ParamSpec S{PK::Value, Type::Str};
    const ParamSpec B{PK::Value, Type::Bool};
    const ParamSpec A{PK::Value, Type::Any};
    const ParamSpec V{PK::Name,  Type::Any};   // variable name

    static const std::vector<FuncSpec> t = [&] {
        std::vector<FuncSpec> v;
        auto add = [&](const char* disp, const char* standard, const char* purpose,
                       const char* ex, std::vector<ParamSpec> p, size_t minA,
                       bool var, Type ret, bool control = false) {
            v.push_back({disp, lower(disp), standard, purpose, ex, p, minA, var, ret, control});
        };
        // ---- arithmetic ----
        add("Joggo",   "Add / Sum",      "Add two numbers",           "Joggo(4, 2)  -> 6",        {N, N}, 2, false, Type::Num);
        add("Biyano",  "Subtract",       "Subtract two numbers",      "Biyano(4, 2) -> 2",        {N, N}, 2, false, Type::Num);
        add("Gunon",   "Multiply",       "Multiply two numbers",      "Gunon(4, 2)  -> 8",        {N, N}, 2, false, Type::Num);
        add("Vaggo",   "Divide",         "Divide two numbers",        "Vaggo(9, 2)  -> 4.5",      {N, N}, 2, false, Type::Num);
        add("Vagsesh", "Modulus",        "Find the remainder",        "Vagsesh(9, 2) -> 1",       {N, N}, 2, false, Type::Num);
        // ---- comparison ----
        add("Borok",       "Greater Than",          "Is the first number greater?",   "Borok(4, 2)       -> true",  {N, N}, 2, false, Type::Bool);
        add("Chotok",      "Less Than",             "Is the first number smaller?",   "Chotok(4, 2)      -> false", {N, N}, 2, false, Type::Bool);
        add("Somotul",     "Equal",                 "Check equality of two values",   "Somotul(3, 3)     -> true",  {A, A}, 2, false, Type::Bool);
        add("Osomot",      "Not Equal",             "Check inequality of two values", "Osomot(3, 4)      -> true",  {A, A}, 2, false, Type::Bool);
        add("BoroSoman",   "Greater Than or Equal", "Greater than or equal?",         "BoroSoman(4, 4)   -> true",  {N, N}, 2, false, Type::Bool);
        add("ChotoSoman",  "Less Than or Equal",    "Less than or equal?",            "ChotoSoman(3, 4)  -> true",  {N, N}, 2, false, Type::Bool);
        // ---- logic ----
        add("Ebong",  "Logical AND", "True if both conditions are true",       "Ebong(true, false)  -> false", {B, B}, 2, false, Type::Bool);
        add("Othoba", "Logical OR",  "True if at least one condition is true", "Othoba(true, false) -> true",  {B, B}, 2, false, Type::Bool);
        add("Naaki",  "Logical NOT", "Reverse a logical value",                "Naaki(true)         -> false", {B},    1, false, Type::Bool);
        // ---- input / output / variables ----
        add("Dekhao", "Print / Output",  "Display one or more values",              "Dekhao(\"Sum =\", x)",          {A},    1, true,  Type::Void);
        add("Dhoro",  "Input",           "Read a line from the user into a variable","Dhoro(age, \"Age? \")",       {V, S}, 1, false, Type::Void);
        add("ManDao", "Assign",          "Assign a value to a variable",            "ManDao(x, 10)",                 {V, A}, 2, false, Type::Void);
        add("ManPao", "Get Variable",    "Retrieve a variable's value",             "ManPao(x)  -> 10",              {V},    1, false, Type::Any);
        add("Baraw",  "Increment",       "Increase a numeric variable by one",      "Baraw(x)   (10 -> 11)",         {V},    1, false, Type::Void);
        add("Komaw",  "Decrement",       "Decrease a numeric variable by one",      "Komaw(x)   (11 -> 10)",         {V},    1, false, Type::Void);
        // ---- math helpers ----
        add("Borgo",     "Square",         "Calculate the square of a number", "Borgo(5)     -> 25", {N}, 1, false, Type::Num);
        add("Ghono",     "Cube",           "Calculate the cube of a number",   "Ghono(3)     -> 27", {N}, 1, false, Type::Num);
        add("Borgomul",  "Square Root",    "Find the square root",             "Borgomul(81) -> 9",  {N}, 1, false, Type::Num);
        add("Porom",     "Absolute Value", "Return the absolute value",        "Porom(-7)    -> 7",  {N}, 1, false, Type::Num);
        // ---- strings ----
        add("Jora",    "String Concatenation", "Join two values into text",        "Jora(\"Zy\", \"vo\") -> Zyvo", {A, A}, 2, false, Type::Str);
        add("Doirgho", "String Length",        "Count characters in a string",     "Doirgho(\"Zyvo\")   -> 4",      {S},    1, false, Type::Num);
        add("Ulto",    "Reverse String",       "Reverse a string",                 "Ulto(\"abc\")       -> cba",    {S},    1, false, Type::Str);
        add("Barbar",  "Repeat String",        "Repeat a string several times",    "Barbar(\"Hi\", 3)   -> HiHiHi", {S, N}, 2, false, Type::Str);
        // ---- control flow (block statements) ----
        add("Jodi",  "Conditional Statement", "Run a block only if a condition is true (optional 'nahole' else-block)",
            "Jodi(Borok(a, b)) { ... } nahole { ... }", {B}, 1, false, Type::Void, true);
        add("Ghuro", "Loop",                  "Repeat a block while a condition is true",
            "Ghuro(Chotok(i, 5)) { ... }",              {B}, 1, false, Type::Void, true);
        return v;
    }();
    return t;
}

inline const FuncSpec* findFunction(const std::string& name) {
    const std::string k = lower(name);
    for (const auto& f : functionTable())
        if (f.key == k) return &f;
    return nullptr;
}

// Edit distance, used for "Did you mean ...?" hints.
inline int editDistance(const std::string& a, const std::string& b) {
    std::vector<int> prev(b.size() + 1), cur(b.size() + 1);
    for (size_t j = 0; j <= b.size(); ++j) prev[j] = (int)j;
    for (size_t i = 1; i <= a.size(); ++i) {
        cur[0] = (int)i;
        for (size_t j = 1; j <= b.size(); ++j) {
            int cost = a[i - 1] == b[j - 1] ? 0 : 1;
            cur[j] = std::min({prev[j] + 1, cur[j - 1] + 1, prev[j - 1] + cost});
        }
        std::swap(prev, cur);
    }
    return prev[b.size()];
}

inline std::string suggestFunction(const std::string& name) {
    const std::string k = lower(name);
    std::string best;
    int bestD = 3;
    for (const auto& f : functionTable()) {
        int d = editDistance(k, f.key);
        if (d < bestD) { bestD = d; best = f.display; }
    }
    return best;
}

} // namespace zyvo
