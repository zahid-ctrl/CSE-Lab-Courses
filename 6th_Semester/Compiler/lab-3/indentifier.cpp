#include <iostream>
#include <string>
#include <vector>
#include <regex>
#include <set>
using namespace std;

int main() {
    string source;

    cout << "Enter source code: ";
    getline(cin, source);

    set<string> identifiers;

    vector<string> arithmetic;
    vector<string> relational;
    vector<string> logical;
    vector<string> assignment;

    set<string> keywords = {
        "int", "float", "double", "char",
        "if", "else", "for", "while",
        "return", "void", "long", "short", "bool"
    };

    regex tokenPattern(
        R"([A-Za-z_][A-Za-z0-9_]*|==|!=|<=|>=|\+\+|--|&&|\|\||[+\-*/%<>=])"
    );

    sregex_iterator it(source.begin(), source.end(), tokenPattern);
    sregex_iterator end;

    for (; it != end; ++it) {

        string t = it->str();

        if (regex_match(t,
            regex(R"([A-Za-z_][A-Za-z0-9_]*)"))) {

            if (!keywords.count(t))
                identifiers.insert(t);
        }

        else if (t == "+" || t == "-" ||
                 t == "*" || t == "/" || t == "%") {

            arithmetic.push_back(t);
        }

        else if (t == "<" || t == ">" ||
                 t == "<=" || t == ">=" ||
                 t == "==" || t == "!=") {

            relational.push_back(t);
        }

        else if (t == "&&" || t == "||" || t == "!") {

            logical.push_back(t);
        }

        else if (t == "=") {

            assignment.push_back(t);
        }
    }

    cout << "\nIdentifiers:\n";

    for (const auto& x : identifiers)
        cout << x << '\n';

    cout << "\nOperators:\n";

    for (const auto& x : assignment)
        cout << x << ' ';

    for (const auto& x : arithmetic)
        cout << x << ' ';

    for (const auto& x : relational)
        cout << x << ' ';

    for (const auto& x : logical)
        cout << x << ' ';

    cout << "\n";

    cout << "\nOperator Classification:\n";

    cout << "Assignment: ";

    if (assignment.empty())
        cout << "None";
    else
        for (const auto& x : assignment)
            cout << x << ' ';

    cout << "\nArithmetic: ";

    if (arithmetic.empty())
        cout << "None";
    else
        for (const auto& x : arithmetic)
            cout << x << ' ';

    cout << "\nRelational: ";

    if (relational.empty())
        cout << "None";
    else
        for (const auto& x : relational)
            cout << x << ' ';

    cout << "\nLogical: ";

    if (logical.empty())
        cout << "None";
    else
        for (const auto& x : logical)
            cout << x << ' ';

    cout << '\n';

    return 0;
}
