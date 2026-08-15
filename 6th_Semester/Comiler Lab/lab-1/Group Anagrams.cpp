#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
using namespace std;
vector<vector<string>> groupAnagrams(vector<string>& strs)
{
map<string, vector<string>> mp;

for(string s : strs)
{
string key = s;
sort(key.begin(), key.end());
mp[key].push_back(s);
}
vector<vector<string>> result;
for(auto group : mp)
{
result.push_back(group.second);
}
return result;
}
int main()
{
vector<string> strs =
{
"eat","tea","tan","ate","nat","bat"
};
vector<vector<string>> ans = groupAnagrams(strs);
for(auto group : ans)
{
cout << "[ ";
for(string s : group)

cout << s << " ";
cout << "]\n";
}
return 0;
}
