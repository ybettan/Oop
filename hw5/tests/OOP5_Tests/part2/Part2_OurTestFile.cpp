#include <iostream>
#include <cassert>

//
// Created by Hadas on 20/06/2018.
//
#include "Stream.h"
#include <list>
#include <map>
#include <vector>
#include <cstring>
#include <set>

void super_duper_basic_test() {
    std::cout << "super_duper_basic_test...";
    std::vector<int> vec = {1, 2, 3, 4, 5, 6, 7, 8};
    std::vector<int *> vec2;
    for (int i = 0; i < 8; i++) {
        vec2.push_back(&vec[i]);
    }
    Stream<int> st = Stream<int>::of(vec2);
    auto filter_fun = [](const int *n) -> bool {
        return ((*n) % 2 == 0);
    };
    st = st.filter(filter_fun);
    std::vector<int *> res = st.collect<std::vector<int *>>();

    assert(res.size() == 4);
    for (int i = 0; i < 4; i++) {
        assert(*res[i] = (i + 1) * 2);
    }

    std::map<char, int *> mymap;
    mymap['a'] = &vec[0];
    mymap['b'] = &vec[1];
    mymap['c'] = &vec[2];
    mymap['d'] = &vec[3];
    auto res2 = Stream<int>::of(mymap).filter(filter_fun).collect<std::list<int *>>();
    for (int i = 0; i < 2; i++) {
        auto it = res2.begin();
        std::advance(it, i);
        assert(*(*it) = (i + 1) * 2);
    }

    auto res3 = Stream<int>::of(vec2).collect<std::vector<int *>>();
    for (int i = 0; i < 8; i++) {
        assert(res3[i] == &vec[i]);
    }

    std::cout << "PASSED" << std::endl;
}

void basic_filter_test() {
    std::cout << "basic_map_test...";
    std::vector<std::string> vec = {"hadas", "michael", "oop", "natan", "omer"};
    std::vector<std::string *> vec_ptr = {&vec[0], &vec[1], &vec[2], &vec[3], &vec[4]};

    auto fun = [](const std::string *s) -> bool {
        return s->size() > 4;
    };
    auto res = Stream<std::string>::of(vec_ptr).filter(fun).collect<std::vector<std::string *>>();

    assert(res.size() == 3);
    assert(res[0] == &vec[0]);
    assert(res[1] == &vec[1]);
    assert(res[2] == &vec[3]);

    std::cout << "PASSED" << std::endl;
}

void basic_map_test() {
    std::cout << "basic_map_test...";

    //same type map test
    std::vector<int> vec = {0, 1, 2, 3, 4, 5, 6};
    std::vector<int *> vec_ptr;
    for (int i = 0; i < 7; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    auto fun = [](const int *n) -> int * {
        return new int((*n) * 2);
    };
    auto res = Stream<int>::of(vec_ptr).map<int>(fun).collect<std::vector<int *>>();

    assert(res.size() == 7);
    for (int i = 0; i < 7; i++) {
        assert(*res[i] == i * 2);
        delete res[i];
    }

    // different type
    auto fun2 = [](const int *n) -> char * {
        return new char('a');
    };

    auto res2 = Stream<int>::of(vec_ptr).map<char>(fun2).collect<std::vector<char *>>();
    assert(res2.size() == 7);
    for (int i = 0; i < 7; i++) {
        assert(*res2[i] == 'a');
        delete res2[i];
    }

    std::cout << "PASSED" << std::endl;
}

void basic_distinct_test() {
    std::cout << "basic_distinct_test...";
    std::vector<int> vec = {1, 2, 3, 4, 4, 1};
    std::vector<int *> vec_ptr;

    for (int i = 0; i < 6; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    auto fun = [](const int *n1, const int *n2) -> bool {
        return (((*n1) % 2) == ((*n2) % 2));
    };
    auto res = Stream<int>::of(vec_ptr).distinct(fun).collect<std::vector<int *>>();

    assert(res.size() == 2);
    assert(*res[0] == 1);
    assert(*res[1] == 2);

    auto res2 = Stream<int>::of(vec_ptr).distinct().collect<std::vector<int *>>();
    assert(res2.size() == 4);
    assert(*res2[0] == 1);
    assert(*res2[1] == 2);
    assert(*res2[2] == 3);
    assert(*res2[3] == 4);

    std::cout << "PASSED" << std::endl;
}

void basic_sorted_test() {
    std::cout << "basic_sorted_test...";
    std::vector<int> vec = {3, 2, 1, 4};
    std::vector<int *> vec_ptr;

    for (int i = 0; i < 4; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    auto res = Stream<int>::of(vec_ptr).sorted().collect<std::vector<int *>>();
    assert(res.size() == 4);
    assert(*res[0] == 1);
    assert(*res[1] == 2);
    assert(*res[2] == 3);
    assert(*res[3] == 4);

    std::cout << "PASSED" << std::endl;
}

void basic_forEach_test() {
    std::cout << "basic_forEach_test...";
    std::vector<int> vec = {5, 4, 3, 2, 1};
    std::vector<int *> vec_ptr;
    for (int i = 0; i < 5; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    std::vector<int *> *res = new std::vector<int *>();
    auto fun = [res](int *n) -> void {
        res->push_back(n);
    };

    Stream<int>::of(vec_ptr).forEach(fun);

    for (int i = 0; i < 5; i++) {
        assert((*res)[i] == vec_ptr[i]);
    }

    delete res;

    std::cout << "PASSED" << std::endl;
}

void basic_reduce_test() {
    std::cout << "basic_reduce_test...";
    std::vector<int> vec = {10, 20, 30, 40};
    std::vector<int *> vec_ptr;
    for (int i = 0; i < 4; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    int num = 0;
    std::function<int *(const int *, const int *)> fun = [](const int *n1, const int *n2) -> int * {
        return new int(*n1 + *n2);
    };
    int *res = Stream<int>::of(vec_ptr).reduce(&num, fun);
    assert(*res == 100);

    std::cout << "PASSED" << std::endl;
}

void basic_min_max_test() {
    std::cout << "basic_min_max_test...";
    std::vector<int> vec = {10, 20, 30, 40};
    std::vector<int *> vec_ptr;
    for (int i = 0; i < 4; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    assert(*(Stream<int>::of(vec_ptr).min()) == 10);
    assert(*(Stream<int>::of(vec_ptr).max()) == 40);

    std::cout << "PASSED" << std::endl;
}

void basic_count_test() {
    std::cout << "basic_count_test...";
    std::vector<int> vec = {10, 20, 30, 40};
    std::vector<int *> vec_ptr;
    for (int i = 0; i < 4; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    assert(Stream<int>::of(vec_ptr).count() == 4);

    std::cout << "PASSED" << std::endl;

}

void basic_anyMatch_allMatch_test() {
    std::cout << "basic_anyMatch_allMatch_test...";
    std::vector<int> vec = {10, 20, 30, 40};
    std::vector<int *> vec_ptr;
    for (int i = 0; i < 4; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    //all match
    std::function<bool(const int *)> fun1 = [](const int *n) -> bool {
        return (*n % 10 == 0);
    };
    bool res = Stream<int>::of(vec_ptr).allMatch(fun1);
    assert(res == true);


    std::function<bool(const int *)> fun2 = [](const int *n) -> bool {
        return (*n == 10 || *n == 20 || *n == 30);
    };
    res = Stream<int>::of(vec_ptr).allMatch(fun2);
    assert(res == false);

    //any match
    std::function<bool(const int *)> fun3 = [](const int *n) -> bool {
        return (*n == 20);
    };
    res = Stream<int>::of(vec_ptr).anyMatch(fun3);
    assert(res == true);

    std::function<bool(const int *)> fun4 = [](const int *n) -> bool {
        return (*n == 99);
    };
    res = Stream<int>::of(vec_ptr).anyMatch(fun4);
    assert(res == false);

    std::cout << "PASSED" << std::endl;

}

void basic_findFirst_test() {
    std::cout << "basic_findFirst_test...";
    std::vector<int> vec = {1, 2, 3, 4};
    std::vector<int *> vec_ptr;
    for (int i = 0; i < 4; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    auto fun = [](const int *n) -> bool {
        return ((*n) % 2 == 0);
    };

    int *num = Stream<int>::of(vec_ptr).findFirst(fun);
    assert(*num == 2);

    std::vector<std::string> vec2 = {"a", "bb", "ccc", "ddd"};
    std::vector<std::string *> vec_ptr2;
    for (int i = 0; i < 4; i++) {
        vec_ptr2.push_back(&vec2[i]);
    }
    auto fun2 = [](const std::string *s) -> bool {
        return (s->length() > 2);
    };
    auto fun3 = [](const std::string *s) -> bool {
        return (s->length() > 3);
    };

    std::string *str = Stream<std::string>::of(vec_ptr2).findFirst(fun2);
    assert(str == vec_ptr2[2]);
    str = Stream<std::string>::of(vec_ptr2).findFirst(fun3);
    assert(str == nullptr);

    std::cout << "PASSED" << std::endl;


}

void testSimpleMapCollection() {
    std::cout << "testSimpleMapCollection...";

    std::map<std::string, int *> mapCollection;
    int values[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    mapCollection.insert(std::pair<std::string, int *>("a", &values[1]));
    mapCollection.insert(std::pair<std::string, int *>("c", &values[3]));
    mapCollection.insert(std::pair<std::string, int *>("e", &values[5]));
    mapCollection.insert(std::pair<std::string, int *>("b", &values[2]));
    mapCollection.insert(std::pair<std::string, int *>("f", &values[6]));

    Stream<int> s = Stream<int>::of(mapCollection);
    std::set<int *> res = s.collect<std::set<int *>>(); //this line doesn't compile because set doesn't have push back
    assert(res.count(&values[1]) == 1);
    assert(res.count(&values[3]) == 1);
    assert(res.count(&values[5]) == 1);
    assert(res.count(&values[2]) == 1);
    assert(res.count(&values[6]) == 1);
    assert(res.size() == 5);

    std::cout << "PASSED" << std::endl;
}


void testCombinedOperations() {
    std::cout << "testCombinedOperations...";

    char *charArray = (char *) "bfzzzziadzzzzeczzghzz";
    std::vector<char *> values;
    for (int i = 0; i < strlen(charArray); i++) {
        values.push_back(charArray + i);
    }


    std::vector<char *> res = Stream<char>::of(values).sorted().distinct().sorted().collect<std::vector<char *>>();
    assert(res.size() == 10);
    assert(*res.at(0) == 'a');
    assert(*res.at(1) == 'b');
    assert(*res.at(2) == 'c');
    assert(*res.at(3) == 'd');
    assert(*res.at(4) == 'e');
    assert(*res.at(5) == 'f');
    assert(*res.at(6) == 'g');
    assert(*res.at(7) == 'h');
    assert(*res.at(8) == 'i');
    assert(*res.at(9) == 'z');

    res = Stream<char>::of(values).sorted().distinct().sorted().distinct().collect<std::vector<char *>>();
    assert(res.size() == 10);
    assert(*res.at(0) == 'a');
    assert(*res.at(1) == 'b');
    assert(*res.at(2) == 'c');
    assert(*res.at(3) == 'd');
    assert(*res.at(4) == 'e');
    assert(*res.at(5) == 'f');
    assert(*res.at(6) == 'g');
    assert(*res.at(7) == 'h');
    assert(*res.at(8) == 'i');
    assert(*res.at(9) == 'z');

    auto funToAscii = [](const char *ch) -> int * {
        char c = *ch;
        return new int((int) (c));
    };

    std::vector<int *> res2 = Stream<char>::of(values).map<int>(funToAscii).distinct().collect<std::vector<int *>>();
    int size = Stream<char>::of(values).map<int>(funToAscii).distinct().count();
    assert(size == 10);
    assert(res2.size() == 10);
    assert(*res2.at(0) == (int) 'b');
    assert(*res2.at(1) == (int) 'f');
    assert(*res2.at(2) == (int) 'z');
    assert(*res2.at(3) == (int) 'i');
    assert(*res2.at(4) == (int) 'a');
    assert(*res2.at(5) == (int) 'd');
    assert(*res2.at(6) == (int) 'e');
    assert(*res2.at(7) == (int) 'c');
    assert(*res2.at(8) == (int) 'g');
    assert(*res2.at(9) == (int) 'h');

    std::cout << "PASSED" << std::endl;
}

void test_laziness(){
    std::cout << "test_laziness...";

    auto fun = [](const int *n) -> bool {
        throw -1;
    };

    std::vector<int> vec = {10, 20, 30, 40};
    std::vector<int *> vec_ptr;
    for (int i = 0; i < 4; i++) {
        vec_ptr.push_back(&vec[i]);
    }

    auto st = Stream<int>::of(vec_ptr).filter(fun).filter(fun);
    bool flag = false;

    try{
        st.collect<std::vector<int *>>();
    }
    catch(int n){
        if (n == -1) flag = true;
    }

    assert(flag);


    std::cout << "PASSED" << std::endl;
}



int main() {

    super_duper_basic_test();
    basic_filter_test();
    basic_map_test();
    basic_distinct_test();
    basic_sorted_test();
    basic_forEach_test();
    basic_reduce_test();
    basic_min_max_test();
    basic_count_test();
    basic_anyMatch_allMatch_test();
    basic_findFirst_test();
    testSimpleMapCollection();
    testCombinedOperations();
    test_laziness();
}
