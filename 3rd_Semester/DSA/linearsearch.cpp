#include <iostream>
using namespace std;
int linearsearch(int arr[],int key,int n)
{
    for(int i=0;i<n;i++)
        if(arr[i]==key)return i;
    return -1;
}
int main()
{
    int arr[]={3,4,6,2,9};
    int key=2;
    int n=5;
    int result=linearsearch(arr,key,n);
    cout<<"found at index "<<result<<endl;
    return 0;
}
