//
// Created by מיכאל on 22/06/2018.
//

#include "MatrixOperations.h"
int main(){

    typedef List<List<Int<1>, Int<2>>> l1;
    typedef List<List<Int<2>>, List<Int<2>>> l2;
    typedef typename Add<l1, l2>::result l;

    return 0;
}