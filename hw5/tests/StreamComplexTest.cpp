#include "Stream.h"

//#include <cassert>
#include <assert.h>
#include <iostream>
#include <map>
#include <set>
#include <list>
#include <stack>
#include <deque>

using namespace std;

struct Cel {

    int x;

    Cel(int x) : x(x) {}

    bool operator==(const Cel &other) const {
        return x == other.x;
    }

    bool operator<(const Cel &other) const {
        return x < other.x;
    }

    bool operator>(const Cel &other) = delete;
};

struct Pair {

    string name;
    Cel c;

    Pair(string name, Cel c) : name(name), c(c) {}

    bool operator==(const Pair &other) const {
        return c == other.c;
    }

    bool operator<(const Pair &other) const {
        return c < other.c;
    }

    bool operator>(const Pair &other) = delete;
};


void testInit(vector<Pair*> &vec, map<char, Pair*> &m) {

    vec.push_back(new Pair("cell_1", Cel(1)));
    vec.push_back(new Pair("cell_2", Cel(2)));
    vec.push_back(new Pair("cell_3", Cel(3)));
    vec.push_back(new Pair("cell_4", Cel(2)));
    vec.push_back(new Pair("cell_5", Cel(8)));
    vec.push_back(new Pair("cell_6", Cel(10)));
    vec.push_back(new Pair("cell_7", Cel(12)));
    vec.push_back(new Pair("cell_8", Cel(10)));
    vec.push_back(new Pair("cell_9", Cel(10)));
    vec.push_back(new Pair("cell_10", Cel(1)));

    m['a'] = new Pair("cell_1", Cel(1));
    m['b'] = new Pair("cell_2", Cel(2));
    m['c'] = new Pair("cell_3", Cel(3));
    m['d'] = new Pair("cell_4", Cel(2));
    m['e'] = new Pair("cell_5", Cel(8));
    m['f'] = new Pair("cell_6", Cel(10));
    m['g'] = new Pair("cell_7", Cel(12));
    m['h'] = new Pair("cell_8", Cel(10));
    m['i'] = new Pair("cell_9", Cel(10));
    m['j'] = new Pair("cell_10", Cel(1));
    
}

void testComplex(Stream<Pair> s) {

    int counter = 0;

    auto res = s.filter([&](const Pair *p)
                        {
                            counter++;
                            return *p < Pair("tmp", Cel(12));
                        })
                .distinct()
                .sorted()
                .distinct([&](const Pair *p1, const Pair *p2)
                        {
                            counter++;
                            return p1->name == p2->name;
                        })
                .map<Cel>([&](const Pair *p)
                        {
                            counter++;
                            return new Cel(p->c);
                        })
                .sorted([&](const Cel *c1, const Cel *c2)
                        {
                            counter++;
                            return *c2 < *c1;
                        })
                .map<double>([&](const Cel *c)
                        {
                            counter++;
                            return new double(c->x + 0.5);
                        });
                
    /* lazynes check */
    assert(counter == 0);
    res.min();
    assert(counter > 0);

    /* collect */
    vector<double*> resVec = res.collect<vector<double*>>();
    assert(resVec.size() == 5);
    assert(*resVec[0] == 10.5);
    assert(*resVec[1] == 8.5);
    assert(*resVec[2] == 3.5);
    assert(*resVec[3] == 2.5);
    assert(*resVec[4] == 1.5);

    /* forEach */
    vector<double> forEachVec;
    res.forEach([&](double *d) {forEachVec.push_back(*d + 0.2);});
    assert(forEachVec.size() == 5);
    assert(forEachVec[0] == 10.7);
    assert(forEachVec[1] == 8.7);
    assert(forEachVec[2] == 3.7);
    assert(forEachVec[3] == 2.7);
    assert(forEachVec[4] == 1.7);

    /* reduce */
    double initial = 0;
    double *sum = res.reduce(&initial, [](const double *d1, const double *d2)
                    {
                        return new double(*d1 + *d2);
                    });
    assert(*sum == 26.5);

    /* min & max */
    assert(*res.min() == 1.5);
    assert(*res.max() == 10.5);

    /* count */
    assert(res.count() == 5);

    /* anyMatch & allMatch */
    assert(res.anyMatch([](const double *d) {return *d == 3.5;}) == true);
    assert(res.anyMatch([](const double *d) {return *d == 3.9;}) == false);
    assert(res.allMatch([](const double *d) {return *d > 0;}) == true);
    assert(res.allMatch([](const double *d) {return *d > 8.0;}) == false);

    /* find first */
    assert(*res.findFirst([](const double *d) {return *d == 3.5;}) == 3.5);
    assert(*res.findFirst([](const double *d) {return *d < 4;}) == 3.5);
    assert(res.findFirst([](const double *d) {return *d > 50;}) == nullptr);
}

int main() {

    /* ASSUMPTIONS:
     * 1. distinct() - T implement operator==
     * 2. sorted() - T implementd operator<
     */

    vector<Pair*> vec;
    map<char, Pair*> m;

    testInit(vec, m);

    Stream<Pair> s1 = Stream<Pair>::of(vec);
    Stream<Pair> s2 = Stream<Pair>::of(m);

    testComplex(s1);
    testComplex(s2);
}



