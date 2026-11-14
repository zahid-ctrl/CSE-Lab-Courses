#include <iostream>
#include <string>
using namespace std;

int main()
{
    string com;
    int a = 0;

    cout << "\nEnter comment: ";
    getline(cin, com);

    if(com[0] == '/')
    {
        if(com[1] == '/')
        {
            cout << "\nIt is a single line comment";
        }
        else if(com[1] == '*')
        {
            for(int i = 2; i < com.length()-1; i++)
            {
                if(com[i] == '*' && com[i+1] == '/')
                {
                    cout << "\nIt is a multiline comment";
                    a = 1;
                    break;
                }
            }

            if(a == 0)
                cout << "\nIt is not a comment";
        }
        else
        {
            cout << "\nIt is not a comment";
        }
    }
    else
    {
        cout << "\nIt is not a comment";
    }

    return 0;
}
