#include "Stream.h"

#include <cassert>
#include <map>
#include <set>
#include <list>
#include <stack>
#include <queue>

using namespace std;

void testCollect() {

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

template <typename T>
void printT(T *t) {
    cout << *t << endl;
}

void testForEach() {

    int intArr[3] = {1, 2, 3};
    vector<int*> intPtrVec;
    for (int i=0 ; i<3 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    Stream<int>::of(intPtrVec).forEach(printT);

    char charArr[2] = {'a', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<2 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    Stream<char>::of(charPtrVec).forEach(&printT);

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    Stream<double>::of(myMap).forEach(printT);
}

template <typename T>
T* sum(const T *t1, const T *t2) {
    T *res = new T;
    *res = *t1 + *t2;
    return res;
}

template <typename T>
T* max(const T *t1, const T *t2) {
    T *res = new T;
    if (*t1 >= *t2)
        *res = *t1;
    else
        *res = *t2;
    return res;
}

void testReduce() {

    int intArr[3] = {1, 2, 3};
    vector<int*> intPtrVec;
    for (int i=0 ; i<3 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    int initialInt = 0;
    int *resInt = Stream<int>::of(intPtrVec).reduce(&initialInt, sum);
    assert(*resInt == 6);

    initialInt = 2;
    resInt = Stream<int>::of(intPtrVec).reduce(&initialInt,
            [](const int* a, const int* b) {int *c = new int;
                                            *c = *a + *b;
                                            return c;});
    assert(*resInt == 8);

    char charArr[2] = {'a', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<2 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    char initialChar = '0';
    char *resChar = Stream<char>::of(charPtrVec).reduce(&initialChar, max);
    assert(*resChar == 'b');

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    double initialDouble = 0;
    double *resDouble = Stream<double>::of(myMap).reduce(&initialDouble, max);
    assert(*resDouble == 30.2);

    initialDouble = 50;
    resDouble = Stream<double>::of(myMap).reduce(&initialDouble, max);
    assert(*resDouble == 50);
}

void testMinMax() {

    int intArr[7] = {7, 2, 4, 3, 6, 5, 1};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    int *minInt = Stream<int>::of(intPtrVec).min();
    int *maxInt = Stream<int>::of(intPtrVec).max();
    assert(*minInt == 1);
    assert(*maxInt == 7);

    char charArr[2] = {'a', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<2 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    char *minChar = Stream<char>::of(charPtrVec).min();
    char *maxChar = Stream<char>::of(charPtrVec).max();
    assert(*minChar == 'a');
    assert(*maxChar == 'b');

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    double *minDouble = Stream<double>::of(myMap).min();
    double *maxDouble = Stream<double>::of(myMap).max();
    assert(*minDouble == 10.2);
    assert(*maxDouble == 30.2);

}

void testCount() {

    int intArr[7] = {7, 2, 4, 3, 6, 5, 1};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    int res = Stream<int>::of(intPtrVec).count();
    assert(res == 7);

    char charArr[2] = {'a', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<2 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    res = Stream<char>::of(charPtrVec).count();
    assert(res == 2);

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    res = Stream<double>::of(myMap).count();
    assert(res == 3);

}

void testAnyAllMatch() {

    int intArr[7] = {7, 2, 4, 3, 6, 5, 1};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    bool res = Stream<int>::of(intPtrVec).anyMatch([](const int *a) {return *a == 4;});
    assert(res == true);

    res = Stream<int>::of(intPtrVec).anyMatch([](const int *a) {return *a == 8;});
    assert(res == false);

    res = Stream<int>::of(intPtrVec).allMatch([](const int *a) {return *a == 4;});
    assert(res == false);

    res = Stream<int>::of(intPtrVec).allMatch([](const int *a) {return *a > 0;});
    assert(res == true);


    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    res = Stream<double>::of(myMap).allMatch([](const double *a) {return *a > 10;});
    assert(res == true);

    res = Stream<double>::of(myMap).anyMatch([](const double *a) {return *a < 2;});
    assert(res == false);

}

void testFindFirst() {

    int intArr[7] = {7, 2, 4, 3, 2, 5, 1};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    int *resInt = Stream<int>::of(intPtrVec).findFirst([](const int *x) {return *x % 2 == 0;});
    assert(resInt == intPtrVec[1]);

    resInt = Stream<int>::of(intPtrVec).findFirst([](const int *x) {return *x == 8;});
    assert(resInt == nullptr);

    char charArr[4] = {'a', 'c' , 'd', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<4 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    char *resChar = Stream<char>::of(charPtrVec).findFirst([](const char *c) {return *c > 'c';});
    assert(resChar == charPtrVec[2]);

    resChar = Stream<char>::of(charPtrVec).findFirst([](const char *c) {return *c > 'd';});
    assert(resChar == nullptr);

    map<char,double*> myMap;
    double a = 10.2, b = 10.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    double *resDouble =
        Stream<double>::of(myMap).findFirst([](const double *e) {return *e == 10.2;});
    assert(resDouble == myMap['a']);


}

int main() {

    testCollect();
    testForEach();
    testReduce();
    testMinMax();
    testCount();
    testAnyAllMatch();
    testFindFirst();
}



