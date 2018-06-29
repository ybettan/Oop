//
// Created by מיכאל on 22/06/2018.
//


#include "MatrixOperations.h"
int main(){


    typedef List<List<Int<1>>> m1;
    typedef List<List<Int<2>>, List<Int<2>>> m2;
    typedef typename Multiply<m1, m2>::result r;
    return 0;
}