#include <iostream>
#include <assert.h>
#include "MatrixOperations.h"

using namespace std;

int main() {

//-----------------------------------------------------------------------------
//                             test Int<>
//-----------------------------------------------------------------------------

    static_assert(Int<3>::value == 3);
    static_assert(Int<0>::value == 0);
    static_assert(Int<-2>::value == -2);


//-----------------------------------------------------------------------------
//                             test List<>
//-----------------------------------------------------------------------------

    /* check list of Int<> */
    typedef List<> intList0;
    typedef List<Int<1>> intList1;
    typedef List<Int<2>, Int<3>> intList2;
    typedef List<Int<4>, Int<5>, Int<6>> intList3;

    static_assert(intList0::size == 0);

    static_assert(intList1::size == 1);
    static_assert(intList1::next::size == 0);
    static_assert(intList1::head::value == 1);

    static_assert(intList2::size == 2);
    static_assert(intList2::next::size == 1);
    static_assert(intList2::next::next::size == 0);
    static_assert(intList2::head::value == 2);
    static_assert(intList2::next::head::value == 3);

    static_assert(intList3::size == 3);
    static_assert(intList3::next::size == 2);
    static_assert(intList3::next::next::size == 1);
    static_assert(intList3::next::next::next::size == 0);
    static_assert(intList3::head::value == 4);
    static_assert(intList3::next::head::value == 5);
    static_assert(intList3::next::next::head::value == 6);

    /* check list of types */
    typedef List<> typeList0;
    typedef List<int> typeList1;
    typedef List<int, char> typeList2;
    typedef List<int, char, double> typeList3;

    static_assert(typeList0::size == 0);

    static_assert(typeList1::size == 1);
    static_assert(typeList1::next::size == 0);
    typedef typeList1::head INT;

    static_assert(typeList2::size == 2);
    static_assert(typeList2::next::size == 1);
    static_assert(typeList2::next::next::size == 0);
    typedef typeList2::next::head CHAR;

    static_assert(typeList3::size == 3);
    static_assert(typeList3::next::size == 2);
    static_assert(typeList3::next::next::size == 1);
    static_assert(typeList3::next::next::next::size == 0);
    typedef typeList3::head INT;
    typedef typeList3::next::head CHAR;
    typedef typeList3::next::next::head DOUBLE;

    /* make sure we got the correct types */
    INT i = 3;
    CHAR c = 't';
    DOUBLE d = 3.14;

//-----------------------------------------------------------------------------
//                         test PrependList<>
//-----------------------------------------------------------------------------

    typedef PrependList<Int<7>, List<Int<8>, Int<9>, Int<10>>> prependedList1;

    static_assert(prependedList1::list::size == 4);
    static_assert(prependedList1::list::next::size == 3);
    static_assert(prependedList1::list::next::next::size == 2);
    static_assert(prependedList1::list::next::next::next::size == 1);
    static_assert(prependedList1::list::next::next::next::next::size == 0);
    static_assert(prependedList1::list::head::value == 7);
    static_assert(prependedList1::list::next::head::value == 8);
    static_assert(prependedList1::list::next::next::head::value == 9);
    static_assert(prependedList1::list::next::next::next::head::value == 10);

    typedef PrependList<Int<11>, List<>> prependedList2;

    static_assert(prependedList2::list::size == 1);
    static_assert(prependedList2::list::next::size == 0);
    static_assert(prependedList2::list::head::value == 11);


//-----------------------------------------------------------------------------
//                            test ListGet<>
//-----------------------------------------------------------------------------
    
    typedef List<Int<1>, Int<2>, Int<3>> intList;

    static_assert(ListGet<0, intList>::value::value == 1);
    static_assert(ListGet<1, intList>::value::value == 2);
    static_assert(ListGet<2, intList>::value::value == 3);

    typedef List<int, char> typeList;

    typedef ListGet<0, typeList>::value INT2;
    typedef ListGet<1, typeList>::value CHAR2;

    INT2 i2 = 4;
    CHAR2 c2 = 'p';

    //FIXME: do we need to check this?
    typedef List<> emptyList;





}




















