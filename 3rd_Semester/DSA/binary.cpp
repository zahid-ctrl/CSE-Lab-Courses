#include <iostream>
using namespace std;
int binarysearch(int arr[],int key,int n)
{
    int low=0,high=n-1;
    while(low<=high)
    {
       int mid=(low+high)/2;
        if(arr[mid]==key) return mid;
        else if(arr[mid]<key) low=mid+1;
        else high=mid-1;
    }
    return -1;
}
int main()
{
    int arr[]={1,3,5,6,7};
    int key=6;
    int n=5;
    int result= binarysearch(arr,key,n);
    cout<<"found at index "<<result<<endl;
    return 0;
}
