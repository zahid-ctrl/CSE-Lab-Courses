// common.hpp - shared definitions: error type and the Zyvo type system
#pragma once
#include <string>
#include <stdexcept>

namespace zyvo {

// Which compiler phase produced an error.
enum class Phase { Lexical, Syntax, Semantic, Runtime };

inline const char* phaseName(Phase p) {
    switch (p) {
        case Phase::Lexical:  return "Lexical error";
        case Phase::Syntax:   return "Syntax error";
        case Phase::Semantic: return "Semantic error";
        case Phase::Runtime:  return "Runtime error";
    }
    return "Error";
}

// An error with a source position (line and column both start at 1).
struct ZyvoError : std::runtime_error {
    Phase phase;
    int line, col;
    ZyvoError(Phase ph, const std::string& msg, int l, int c)
        : std::runtime_error(msg), phase(ph), line(l), col(c) {}
};

// Static types used by the semantic analyser.
enum class Type { Num, Str, Bool, Any, Void };

inline const char* typeName(Type t) {
    switch (t) {
        case Type::Num:  return "number";
        case Type::Str:  return "text";
        case Type::Bool: return "true/false value";
        case Type::Any:  return "any value";
        case Type::Void: return "nothing";
    }
    return "?";
}

} // namespace zyvo
