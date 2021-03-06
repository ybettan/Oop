#include "Stream.h"

#include <cassert>
#include <map>
#include <set>
#include <list>
#include <stack>
#include <deque>
#include <iostream>

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

    cout << "collect test: [PASSED]" << endl;
}

void printInt(const int *x) {
    cout << *x << endl;
}

void printChar(const char *c) {
    cout << *c << endl;
}

void printDouble(const double *d) {
    cout << *d << endl;
}

void testForEach() {

    int intArr[3] = {1, 2, 3};
    vector<int*> intPtrVec;
    for (int i=0 ; i<3 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    vector<int> resIntVec;
    Stream<int>::of(intPtrVec).forEach([&resIntVec](const int *x) {resIntVec.push_back(*x + 10);});
    assert(resIntVec[0] == 11);
    assert(resIntVec[1] == 12);
    assert(resIntVec[2] == 13);

    char charArr[2] = {'a', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<2 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    vector<char> resCharVec;
    Stream<char>::of(charPtrVec).forEach([&resCharVec](const char *x)
            {resCharVec.push_back(*x);});
    assert(resCharVec.size() == 2);
    assert(resCharVec[0] == 'a');
    assert(resCharVec[1] == 'b');

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    vector<double> resDoubleVec;
    Stream<double>::of(myMap).forEach([&resDoubleVec](const double *x)
            {resDoubleVec.push_back(*x - 10);});
    assert(resDoubleVec.size() == 3);
    assert(resDoubleVec[1] == 10.2);
    assert(resDoubleVec[2] == 20.2);

    cout << "forEach test: [PASSED]" << endl;
}

template <typename T>
T* sum(const T *t1, const T *t2) {
    T *res = new T;
    *res = *t1 + *t2;
    return res;
}

char* maxChar(const char *t1, const char *t2) {
    char *res = new char;
    if (*t1 >= *t2)
        *res = *t1;
    else
        *res = *t2;
    return res;
}

double* maxDouble(const double *t1, const double *t2) {
    double *res = new double;
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
    int *resInt = Stream<int>::of(intPtrVec).reduce(&initialInt, sum<int>);
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
    char *resChar = Stream<char>::of(charPtrVec).reduce(&initialChar, maxChar);
    assert(*resChar == 'b');

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    double initialDouble = 0;
    double *resDouble = Stream<double>::of(myMap).reduce(&initialDouble, maxDouble);
    assert(*resDouble == 30.2);

    initialDouble = 50;
    resDouble = Stream<double>::of(myMap).reduce(&initialDouble, maxDouble);
    assert(*resDouble == 50);

    cout << "reduce test: [PASSED]" << endl;
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

    cout << "min and max tests: [PASSED]" << endl;
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

    cout << "count test: [PASSED]" << endl;
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

    cout << "allMatch and anyMatch tests: [PASSED]" << endl;
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

    cout << "findFirst test: [PASSED]" << endl;
}

void testFilter() {

    int intArr[7] = {7, 2, 4, 3, 2, 5, 1};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    vector<int*> resIntPtrVec = Stream<int>::of(intPtrVec)
                    .filter([](const int *x) {return *x % 2 == 0;})
                    .collect<vector<int*>>();
    assert(resIntPtrVec.size() == 3);
    assert(*resIntPtrVec[0] == 2);
    assert(*resIntPtrVec[1] == 4);
    assert(*resIntPtrVec[2] == 2);

    char charArr[4] = {'a', 'c' , 'd', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<4 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    set<char*> resCharPtrSet = Stream<char>::of(charPtrVec)
                    .filter([](const char *c) {return *c == 'd';})
                    .collect<set<char*>>();
    assert(resCharPtrSet.size() == 1);
    assert(resCharPtrSet.find(charPtrVec[2]) != resCharPtrSet.end());
    assert(resCharPtrSet.find(charPtrVec[1]) == resCharPtrSet.end());

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    deque<double*> resIntPtrDeque = Stream<double>::of(myMap)
                    .filter([](const double *d) {return *d > 15;})
                    .collect<deque<double*>>();
    assert(resIntPtrDeque.size() == 2);
    assert(*resIntPtrDeque.front() == 20.2); resIntPtrDeque.pop_front();
    assert(*resIntPtrDeque.front() == 30.2); resIntPtrDeque.pop_front();
    assert(resIntPtrDeque.size() == 0);

    cout << "filter test: [PASSED]" << endl;
}

void testDistinct() {

    int intArr[7] = {7, 2, 4, 3, 2, 5, 1};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    vector<int*> resIntPtrVec = Stream<int>::of(intPtrVec)
                    .distinct()
                    .collect<vector<int*>>();
    assert(resIntPtrVec.size() == 6);
    std::sort(resIntPtrVec.begin(), resIntPtrVec.end(),
            [](const int *x1, const int *x2) {return *x1 < *x2;});
    assert(*resIntPtrVec[0] == 1);
    assert(*resIntPtrVec[1] == 2);
    assert(*resIntPtrVec[2] == 3);
    assert(*resIntPtrVec[3] == 4);
    assert(*resIntPtrVec[4] == 5);
    assert(*resIntPtrVec[5] == 7);

    char charArr[4] = {'a', 'c' , 'd', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<4 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    set<char*> resCharPtrSet = Stream<char>::of(charPtrVec)
                    .distinct([](const char *c1, const char *c2) {return std::abs(*c1-*c2) <= 1;})
                    .collect<set<char*>>();
    assert(resCharPtrSet.size() == 2);
    assert(resCharPtrSet.find(charPtrVec[0]) != resCharPtrSet.end());
    assert(resCharPtrSet.find(charPtrVec[1]) != resCharPtrSet.end());
    assert(resCharPtrSet.find(charPtrVec[2]) == resCharPtrSet.end());
    assert(resCharPtrSet.find(charPtrVec[3]) == resCharPtrSet.end());

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    deque<double*> resIntPtrDeque = Stream<double>::of(myMap)
                    .distinct()
                    .collect<deque<double*>>();
    assert(resIntPtrDeque.size() == 3);
    assert(*resIntPtrDeque.front() == 10.2); resIntPtrDeque.pop_front();
    assert(*resIntPtrDeque.front() == 20.2); resIntPtrDeque.pop_front();
    assert(*resIntPtrDeque.front() == 30.2); resIntPtrDeque.pop_front();
    assert(resIntPtrDeque.size() == 0);

    cout << "distinct test: [PASSED]" << endl;
}

void testSorted() {

    int intArr[7] = {7, 2, 4, 3, 2, 5, 1};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    vector<int*> resIntPtrVec = Stream<int>::of(intPtrVec)
                    .sorted()
                    .collect<vector<int*>>();
    assert(resIntPtrVec.size() == 7);
    assert(*resIntPtrVec[0] == 1);
    assert(*resIntPtrVec[1] == 2);
    assert(*resIntPtrVec[2] == 2);
    assert(*resIntPtrVec[3] == 3);
    assert(*resIntPtrVec[4] == 4);
    assert(*resIntPtrVec[5] == 5);
    assert(*resIntPtrVec[6] == 7);

    char charArr[4] = {'a', 'c' , 'd', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<4 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    vector<char*> resCharPtrVec = Stream<char>::of(charPtrVec)
                    .sorted([](const char *c1, const char *c2) {return *c2 < *c1;})
                    .collect<vector<char*>>();
    assert(resCharPtrVec.size() == 4);
    assert(*resCharPtrVec[0] == 'd');
    assert(*resCharPtrVec[1] == 'c');
    assert(*resCharPtrVec[2] == 'b');
    assert(*resCharPtrVec[3] == 'a');

    cout << "sorted test: [PASSED]" << endl;
}

void testComplexStream() {

    int intArr[7] = {4, 3, 4, 2, 2, 4, 4};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    vector<int*> resIntPtrVec = Stream<int>::of(intPtrVec)
                    .filter([](const int *x) {return (*x == 2) || (*x == 4);})
                    .distinct()
                    .sorted()
                    .collect<vector<int*>>();
    assert(resIntPtrVec.size() == 2);
    std::sort(resIntPtrVec.begin(), resIntPtrVec.end(),
            [](const int *x1, const int *x2) {return *x1 < *x2;});
    assert(*resIntPtrVec[0] == 2);
    assert(*resIntPtrVec[1] == 4);

    int intArr2[7] = {4, 3, 4, 2, 2, 4, 8};
    vector<int*> intPtrVec2;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec2.push_back(intArr2 + i);
    }

    vector<double*> resDoublePtrVec = Stream<int>::of(intPtrVec2)
                    .filter([](const int *x) {return (*x == 2) || (*x == 4);})
                    .map<int>([](const int *x) {return new int(*x+2);})
                    .map<double>([](const int *x) {return new double(*x+0.5);})
                    .filter([](const double *x) {return *x > 5;})
                    .sorted()
                    .collect<vector<double*>>();

    assert(resDoublePtrVec.size() == 3);
    assert(*resDoublePtrVec[0] == 6.5);
    assert(*resDoublePtrVec[1] == 6.5);
    assert(*resDoublePtrVec[2] == 6.5);

    cout << "complexStream test: [PASSED]" << endl;
}

void testMap() {

    int intArr[7] = {7, 2, 4, 3, 2, 5, 1};
    vector<int*> intPtrVec;
    for (int i=0 ; i<7 ; i++) {
        intPtrVec.push_back(intArr + i);
    }

    vector<double*> resToDoubleVec = Stream<int>::of(intPtrVec)
                    .map<double>([](const int *x) {return new double(*x + 0.5);})
                    .collect<vector<double*>>();
    assert(resToDoubleVec.size() == 7);
    std::sort(resToDoubleVec.begin(), resToDoubleVec.end(),
            [](const double *x1, const double *x2) {return *x1 < *x2;});
    assert(*resToDoubleVec[0] == 1.5);
    assert(*resToDoubleVec[1] == 2.5);
    assert(*resToDoubleVec[2] == 2.5);
    assert(*resToDoubleVec[3] == 3.5);
    assert(*resToDoubleVec[4] == 4.5);
    assert(*resToDoubleVec[5] == 5.5);
    assert(*resToDoubleVec[6] == 7.5);

    char charArr[4] = {'a', 'c' , 'd', 'b'};
    vector<char*> charPtrVec;
    for (int i=0 ; i<4 ; i++) {
        charPtrVec.push_back(charArr + i);
    }

    vector<char*> resCharPtrVec = Stream<char>::of(charPtrVec)
                    .map<char>([](const char *c) {return new char(*c);})
                    .collect<vector<char*>>();
    std::sort(resCharPtrVec.begin(), resCharPtrVec.end(),
            [](const char *x1, const char *x2) {return *x1 < *x2;});
    assert(resCharPtrVec.size() == 4);
    assert(*resCharPtrVec[0] == 'a');
    assert(*resCharPtrVec[1] == 'b');
    assert(*resCharPtrVec[2] == 'c');
    assert(*resCharPtrVec[3] == 'd');

    resCharPtrVec = Stream<char>::of(charPtrVec)
                    .map<char>([](const char *c) {return new char(*c + 2);})
                    .collect<vector<char*>>();
    std::sort(resCharPtrVec.begin(), resCharPtrVec.end(),
            [](const char *x1, const char *x2) {return *x1 < *x2;});
    assert(resCharPtrVec.size() == 4);
    assert(*resCharPtrVec[0] == 'c');
    assert(*resCharPtrVec[1] == 'd');
    assert(*resCharPtrVec[2] == 'e');
    assert(*resCharPtrVec[3] == 'f');

    map<char,double*> myMap;
    double a = 10.2, b = 20.2, c = 30.2, d = 40.2;
    myMap['a'] = &a;
    myMap['b'] = &b;
    myMap['c'] = &c;

    deque<int*> resIntPtrDeque = Stream<double>::of(myMap)
                    .map<int>([](const double *x) {return new int(*x);})
                    .collect<deque<int*>>();
    assert(resIntPtrDeque.size() == 3);
    assert(*resIntPtrDeque.front() == 10); resIntPtrDeque.pop_front();
    assert(*resIntPtrDeque.front() == 20); resIntPtrDeque.pop_front();
    assert(*resIntPtrDeque.front() == 30); resIntPtrDeque.pop_front();
    assert(resIntPtrDeque.size() == 0);

    cout << "map test: [PASSED]" << endl;
}

//void testLazyness() {
//
//    int intArr[7] = {4, 3, 4, 2, 2, 4, 4};
//    vector<int*> intPtrVec;
//    for (int i=0 ; i<7 ; i++) {
//        intPtrVec.push_back(intArr + i);
//    }
//
//    Stream<int> resIntStream = Stream<int>::of(intPtrVec)
//                    .filter([](const int *x) {return (*x == 2) || (*x == 4);})
//                    .distinct()
//                    .sorted();
//    assert(resIntStream.elements.size() == 7);
//    std::sort(resIntStream.elements.begin(), resIntStream.elements.end(),
//            [](const int *x1, const int *x2) {return *x1 < *x2;});
//    assert(*resIntStream.elements[0] == 2);
//    assert(*resIntStream.elements[1] == 2);
//    assert(*resIntStream.elements[2] == 3);
//    assert(*resIntStream.elements[3] == 4);
//    assert(*resIntStream.elements[4] == 4);
//    assert(*resIntStream.elements[5] == 4);
//    assert(*resIntStream.elements[6] == 4);
//
//    cout << "lazyness test: [PASSED]" << endl;
//}

int main() {

    testCollect();
    testForEach();
    testReduce();
    testMinMax();
    testCount();
    testAnyAllMatch();
    testFindFirst();
    testFilter();
    testMap();
    testDistinct();
    testSorted();
    testComplexStream();
    //testLazyness();
}



