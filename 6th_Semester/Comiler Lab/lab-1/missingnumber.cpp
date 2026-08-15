#include <iostream>
#include <vector>
using namespace std;
int findMissingNumber(vector<int>& nums)
{
int n = nums.size();
int ans = n;
for(int i = 0; i < n; i++)
{
ans ^= i;
ans ^= nums[i];
}
return ans;
}
int main()
{
vector<int> nums = {3,0,1};
cout << "Missing number: "
<< findMissingNumber(nums);
return 0;

}
