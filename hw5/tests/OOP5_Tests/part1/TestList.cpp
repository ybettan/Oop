//
// Created by מיכאל on 21/06/2018.
//

#include "MatrixOperations.h"
#include "TestUtilities.h"
#include <iostream>


void listCreation() {
    typedef List<Int<1>, Int<2>, Int<3>> l;
    static_assert(SameType<l::head, Int<1>>::result == true, "wrong head"); // = Int<1>
    typedef typename l::next listTail; // = List<Int<2>, Int<3>>
    static_assert(l::size == 3, "wrong size"); // = 3

    //empty list:
    static_assert(List<>::size == 0, "size is wrong");

    //list with one element:
    static_assert(List<int>::size == 1, "size is wrong");
    static_assert(List<int>::next::size == 0, "size is wrong");
    static_assert(SameType<List<int>::head, int>::result == true, "head is wrong");

    //more then one element:
    static_assert(SameType<List<int, char, double, float>::head, int>::result == true, "head is wrong");
    static_assert(SameType<List<int, char, double, float>::next::next::head, double>::result == true, "head is wrong");
    static_assert(List<int, char, double, float>::size == 4, "size is wrong");
    static_assert(List<int, char, double, float>::next::size == 3, "size is wrong");
    static_assert(List<int, char, double, float>::next::next::next::next::size == 0, "size is wrong");


};

void prependListTest() {
    //Prepend with empty list:
    typedef PrependList<int, List<>>::list list1;
    static_assert(list1::size == 1, "wrong size");


    typedef PrependList<int, list1>::list list2;
    static_assert(list2::size == 2, "wrong size");
    static_assert(SameType<list2::head, int>::result == true, "wrong head);");
    static_assert(SameType<list2::next, List<int>>::result == true, "wrong next);");
    static_assert(SameType<list2::next, List<>>::result == false, "wrong next);");
    static_assert(SameType<list2::next::next, List<>>::result == true, "wrong next);");


    typedef List<int, double, char, float, int> bigList;
    typedef PrependList<char, bigList>::list list3;
    static_assert(list3::size == 6, "wrong size");
    static_assert(SameType<list3::head, char>::result == true, "wrong head);");
    static_assert(SameType<list3::next, bigList>::result == true, "wrong next);");

}

void getListTest() {
    typedef List<Int<1>, Int<2>, Int<3>> l;
    static_assert(ListGet<0, l>::value::value == 1, "get wrong element");
    static_assert(ListGet<2, l>::value::value == 3, "get wrong element");

}

void setListTest() {
    typedef List<Int<1>, Int<2>, Int<3>, Int<4>, Int<5>> l;
    typedef typename ListSet<0, Int<5>, l>::list listA; // = List<Int<5>, Int<2>, Int<3>, Int<4>, Int<5>>
    static_assert(SameType<listA, List<Int<5>, Int<2>, Int<3>, Int<4>, Int<5>>>::result == true, "wrong list");

    typedef typename ListSet<4, Int<7>, listA>::list listB; // = List<Int<5>, Int<2>, Int<3>, Int<4>, Int<7>>
    typedef typename ListSet<2, Int<0>, listB>::list listC; // = List<Int<5>, Int<2>, Int<0>, Int<4>, Int<7>>

    static_assert(SameType<listC, List<Int<5>, Int<2>, Int<0>, Int<4>, Int<7>>>::result == true, "wrong list");
}

int main() {
    listCreation();
    prependListTest();
    getListTest();
    setListTest();

}
