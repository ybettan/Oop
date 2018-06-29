//
// Created by מיכאל on 21/06/2018.
//

#include "MatrixOperations.h"
#include <limits.h>
#include <iostream>

int main() {
    static_assert(Int<-5>::value == -5, "wrong value");
    static_assert(Int<INT_MAX>::value == INT_MAX, "wrong value");

    return 0;
}
