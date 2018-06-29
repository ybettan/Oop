//
// Created by מיכאל on 22/06/2018.
//

#include <iostream>
#include "MatrixOperations.h"
#include "TestUtilities.h"

int main() {
    typedef List<List<Int<3>>> m1;
    typedef List<List<Int<5>>> m2;
    typedef typename Multiply<m1, m2>::result m3;
    static_assert(SameType<m3, List<List<Int<15>>>>::result == true, "wrong");

    typedef List<List<Int<1>, Int<2> >, List<Int<0>, Int<1> > > m4;
    typedef List<List<Int<0>, Int<7> >, List<Int<8>, Int<0> > > m5;
    typedef typename Multiply<m4, m5>::result m6; // = List<List<Int<16>, Int<7> >, List<Int<8>, Int<0> >
    static_assert(ListGet<0, typename ListGet<0, m6>::value>::value::value == 16, "wrong result");
    static_assert(SameType<m6, List<List<Int<16>, Int<7>>, List<Int<8>, Int<0>>>>::result == true, "wrong");


    typedef List<
            List<Int<4>,Int<3>,Int<6>,Int<8>>,
            List<Int<1>,Int<2>,Int<3>,Int<4>>,
            List<Int<6>,Int<7>,Int<3>,Int<1>>,
            List<Int<-1>,Int<-2>,Int<-7>,Int<0>>

    > m10;

    typedef List<
            List<Int<1>,Int<-2>,Int<7>,Int<81>>,
            List<Int<-1>,Int<-2>,Int<-4>,Int<-1>>,
            List<Int<-4>,Int<3>,Int<7>,Int<6>>,
            List<Int<-1>,Int<-12>,Int<0>,Int<5>>
            >m11;

    typedef typename Multiply<m10,m11>::result m_res;
    static_assert(SameType<m_res,List<
            List<Int<-31>,Int<-92>,Int<58>,Int<397>>,
            List<Int<-17>,Int<-45>,Int<20>,Int<117>>,
            List<Int<-14>,Int<-29>,Int<35>,Int<502>>,
            List<Int<29>,Int<-15>,Int<-48>,Int<-121>>
    >>::result == true,"wrong answer");
}