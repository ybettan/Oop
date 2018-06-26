#include "Stream.h"

#include <cassert>
#include <map>
#include <set>
#include <list>
#include <stack>
#include <queue>

using namespace std;

void testCollect() {

}


int main() {

    int arr[3] = {1, 2, 3};
    vector<int*> intPtrVec;
    for (int i=0 ; i<3 ; i++) {
        intPtrVec.push_back(arr + i);
    }

    vector<int*> resVec = Stream<int>::of(intPtrVec).collect<vector<int*>>();
    assert(*resVec[0] == 1);
    assert(*resVec[1] == 2);
    assert(*resVec[2] == 3);

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    list<double*> resList = Stream<double>::of(myMap).collect<list<double*>>();
    assert(*resList.front() == 10.2); resList.pop_front();
    assert(*resList.front() == 20.2); resList.pop_front();
    assert(*resList.front() == 30.2); resList.pop_front();

    set<double*> resSet = Stream<double>::of(myMap).collect<set<double*>>();
    assert(resSet.find(&a) != resSet.end());
    assert(resSet.find(&b) != resSet.end());
    assert(resSet.find(&c) != resSet.end());
    assert(resSet.find(&d) == resSet.end());

}



