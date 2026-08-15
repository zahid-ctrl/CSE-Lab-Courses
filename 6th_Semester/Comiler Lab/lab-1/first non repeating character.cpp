#include <iostream>
#include <string>
using namespace std;
char firstNonRepeating(string s)
{
int freq[26] = {0};
for(char c : s)
{
freq[c-'a']++;
}
for(char c : s)
{
if(freq[c-'a'] == 1)
return c;
}
return '-';
}
int main()
{

string s = "swiss";
char result = firstNonRepeating(s);
if(result == '-')
cout << "-1";
else
cout << "First non-repeating character: "
<< result;
return 0;
}
