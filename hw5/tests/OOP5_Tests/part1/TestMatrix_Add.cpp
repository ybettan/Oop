//
// Created by מיכאל on 21/06/2018.
//
#include <iostream>
#include "MatrixOperations.h"
#include "TestUtilities.h"

int main() {
    typedef List<List<Int<1>, Int<2>, Int<0> >, List<Int<0>, Int<1>, Int<0> >, List<Int<0>, Int<0>, Int<5> > > matrix1;
    typedef List<List<Int<7>, Int<6>, Int<0> >, List<Int<0>, Int<7>, Int<0> >, List<Int<8>, Int<0>, Int<3> > > matrix2;
    typedef typename Add<matrix1, matrix2>::result matrix3; // = List<List<Int<8>, Int<8>, Int<0> >, List<Int<0>, Int<8>, Int<0> >, List<Int<8>, Int<0>, Int<8> >
    static_assert(
            SameType<matrix3, List<List<Int<8>, Int<8>, Int<0> >, List<Int<0>, Int<8>, Int<0> >, List<Int<8>, Int<0>, Int<8> >>>::result ==
            true, "wrong result");

    typedef List<List<Int<0>, Int<-2>, Int<2>, Int<-4>, Int<4>>> M1;
    typedef List<List<Int<-1>, Int<1>, Int<-3>, Int<3>, Int<-5>>> M2;
    typedef typename Add<M1, M2>::result Res;

    static_assert(SameType<Res, List<List<Int<-1>, Int<-1>, Int<-1>, Int<-1>, Int<-1> >>>::result == true,
                  "wrong result");

}

