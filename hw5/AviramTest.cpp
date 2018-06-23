#include <iostream>

#include "Utilities.h"
#include "MatrixOperations.h"

using namespace std;

void listGetSetPrependCheck() {
    typedef List<Int<1>, Int<2>, Int<3>> list;
    static_assert(list::head::value == 1, "");
    typedef typename list::next listTail;
    static_assert(listTail::head::value == 2, "");
    static_assert(list::size == 3, "");

    typedef PrependList<Int<4>, list>::list newList;
    static_assert(newList::head::value == 4, "");
    static_assert(ListGet<0, list>::value::value == 1, "");
    static_assert(ListGet<2, list>::value::value == 3, "");

    typedef typename ListSet<0, Int<5>, list>::list listA;
    static_assert(ListGet<0, listA>::value::value == 5, "");
    typedef typename ListSet<2, Int<7>, list>::list listB;
    static_assert(ListGet<2, listB>::value::value == 7, "");
    cout << "listGetSetPrependCheck:\tSuccess" << endl;
}


void matAddCheck() {
    typedef List<
                List<Int<1>, Int<2>, Int<0>>,
                List<Int<0>, Int<1>, Int<0>>,
                List<Int<0>, Int<0>, Int<5>>
            > matrix1;
    typedef List<
                List<Int<7>, Int<6>, Int<0>>,
                List<Int<0>, Int<7>, Int<0>>,
                List<Int<8>, Int<0>, Int<3>>
            > matrix2;
    typedef typename Add<matrix1, matrix2>::result matrix3;
    static_assert(matrix3::size == 3, "");
    static_assert(ListGet<0, matrix3>::value::size == 3, "");
    static_assert(ListGet<1, matrix3>::value::size == 3, "");
    static_assert(ListGet<2, matrix3>::value::size == 3, "");
    static_assert(MatrixGet<0, 0, matrix3>::value::value == 8, "");
    static_assert(MatrixGet<0, 1, matrix3>::value::value == 8, "");
    static_assert(MatrixGet<0, 2, matrix3>::value::value == 0, "");
    static_assert(MatrixGet<1, 0, matrix3>::value::value == 0, "");
    static_assert(MatrixGet<1, 1, matrix3>::value::value == 8, "");
    static_assert(MatrixGet<1, 2, matrix3>::value::value == 0, "");
    static_assert(MatrixGet<2, 0, matrix3>::value::value == 8, "");
    static_assert(MatrixGet<2, 1, matrix3>::value::value == 0, "");
    static_assert(MatrixGet<2, 2, matrix3>::value::value == 8, "");

    //Really stupid small case
    typedef List<List<Int<12>>> matrix4;
    typedef List<List<Int<13>>> matrix5;
    typedef typename Add<matrix4, matrix5>::result matrix6;
    static_assert(matrix6::size == 1, "");
    static_assert(ListGet<0, matrix6>::value::size == 1, "");
    static_assert(MatrixGet<0, 0, matrix6>::value::value == 25, "");

    // Weird numbers case
    typedef List<
            List<Int<1>, Int<2>, Int<3>, Int<4>>,
            List<Int<5>, Int<6>, Int<7>, Int<8>>,
            List<Int<9>, Int<10>, Int<11>, Int<12>>
    > matrix7;
    typedef List<
            List<Int<4>, Int<345>, Int<1233>, Int<5>>,
            List<Int<32>, Int<443>, Int<23>, Int<34>>,
            List<Int<436>, Int<57>, Int<3>, Int<345>>
    > matrix8;
    typedef typename Add<matrix7, matrix8>::result matrix9;
    static_assert(matrix9::size == 3, "");
    static_assert(ListGet<0, matrix9>::value::size == 4, "");
    static_assert(ListGet<1, matrix9>::value::size == 4, "");
    static_assert(ListGet<2, matrix9>::value::size == 4, "");
    static_assert(MatrixGet<0, 0, matrix9>::value::value == 5, "");
    static_assert(MatrixGet<0, 1, matrix9>::value::value == 347, "");
    static_assert(MatrixGet<0, 2, matrix9>::value::value == 1236, "");
    static_assert(MatrixGet<0, 3, matrix9>::value::value == 9, "");
    static_assert(MatrixGet<1, 0, matrix9>::value::value == 37, "");
    static_assert(MatrixGet<1, 1, matrix9>::value::value == 449, "");
    static_assert(MatrixGet<1, 2, matrix9>::value::value == 30, "");
    static_assert(MatrixGet<1, 3, matrix9>::value::value == 42, "");
    static_assert(MatrixGet<2, 0, matrix9>::value::value == 445, "");
    static_assert(MatrixGet<2, 1, matrix9>::value::value == 67, "");
    static_assert(MatrixGet<2, 2, matrix9>::value::value == 14, "");
    static_assert(MatrixGet<2, 3, matrix9>::value::value == 357, "");

    cout<< "matAddCheck:\t\tSuccess" << endl;
}

void matMulPdfCheck() {
    typedef List<
            List<Int<1>, Int<2>>,
            List<Int<0>, Int<1>>
    > matrix1;
    typedef List<
            List<Int<0>, Int<7>>,
            List<Int<8>, Int<0>>
    > matrix2;
    typedef typename Multiply<matrix1, matrix2>::result matrix3;
    // dimensions check
    static_assert(matrix3::size == 2, "");
    static_assert(ListGet<0, matrix3>::value::size == 2, "");
    static_assert(ListGet<1, matrix3>::value::size == 2, "");
    // values check
    static_assert(MatrixGet<0, 0, matrix3>::value::value == 16, "");
    static_assert(MatrixGet<0, 1, matrix3>::value::value == 7, "");
    static_assert(MatrixGet<1, 0, matrix3>::value::value == 8, "");
    static_assert(MatrixGet<1, 1, matrix3>::value::value == 0, "");

    //Really stupid small case
    typedef List<List<Int<4>>> matrix4;
    typedef List<List<Int<5>>> matrix5;
    typedef typename Multiply<matrix4, matrix5>::result matrix6;
    static_assert(matrix6::size == 1, "");
    static_assert(ListGet<0, matrix6>::value::size == 1, "");
    static_assert(MatrixGet<0, 0, matrix6>::value::value == 20, "");

    cout<< "matMulPdfCheck:\t\tSuccess" << endl;
}


void matMulCensorCheck() {
    // This example is from Aviv Censor's lecture about matrices
    typedef List<
            List<Int<1>, Int<2>>,
            List<Int<3>, Int<4>>,
            List<Int<5>, Int<6>>
    > matrix1;
    typedef List<
            List<Int<-1>, Int<0>, Int<1>>,
            List<Int<1>, Int<2>, Int<-2>>
    > matrix2;
    typedef typename Multiply<matrix1, matrix2>::result matrix3;
    // dimensions check
    static_assert(matrix3::size == 3, "");
    static_assert(ListGet<0, matrix3>::value::size == 3, "");
    static_assert(ListGet<1, matrix3>::value::size == 3, "");
    static_assert(ListGet<2, matrix3>::value::size == 3, "");
    // values check
    static_assert(MatrixGet<0, 0, matrix3>::value::value == 1, "");
    static_assert(MatrixGet<0, 1, matrix3>::value::value == 4, "");
    static_assert(MatrixGet<0, 2, matrix3>::value::value == -3, "");
    static_assert(MatrixGet<1, 0, matrix3>::value::value == 1, "");
    static_assert(MatrixGet<1, 1, matrix3>::value::value == 8, "");
    static_assert(MatrixGet<1, 2, matrix3>::value::value == -5, "");
    static_assert(MatrixGet<2, 0, matrix3>::value::value == 1, "");
    static_assert(MatrixGet<2, 1, matrix3>::value::value == 12, "");
    static_assert(MatrixGet<2, 2, matrix3>::value::value == -7, "");
    cout<< "matMulCensorCheck:\tSuccess" << endl;
}


void matMulEfratCheck() {
    // These examples are from Efrat Aviram's tutorial about matrices
    typedef List<
            List<Int<1>, Int<2>, Int<3>>
    > A;
    typedef List<
            List<Int<4>>,
            List<Int<5>>,
            List<Int<0>>
    > B;
    typedef List<
            List<Int<1>, Int<2>, Int<1>>,
            List<Int<0>, Int<0>, Int<1>>,
            List<Int<3>, Int<4>, Int<2>>
    > C;

    // #1: calc A*B
    typedef typename Multiply<A, B>::result AB;
    static_assert(AB::size == 1, "");
    static_assert(ListGet<0, AB>::value::size == 1, "");
    static_assert(MatrixGet<0, 0, AB>::value::value == 14, "");

    // #2 calc B*A
    typedef typename Multiply<B, A>::result BA;
    static_assert(BA::size == 3, "");
    static_assert(ListGet<0, BA>::value::size == 3, "");
    static_assert(ListGet<1, BA>::value::size == 3, "");
    static_assert(ListGet<2, BA>::value::size == 3, "");
    static_assert(MatrixGet<0, 0, BA>::value::value == 4, "");
    static_assert(MatrixGet<0, 1, BA>::value::value == 8, "");
    static_assert(MatrixGet<0, 2, BA>::value::value == 12, "");
    static_assert(MatrixGet<1, 0, BA>::value::value == 5, "");
    static_assert(MatrixGet<1, 1, BA>::value::value == 10, "");
    static_assert(MatrixGet<1, 2, BA>::value::value == 15, "");
    static_assert(MatrixGet<2, 0, BA>::value::value == 0, "");
    static_assert(MatrixGet<2, 1, BA>::value::value == 0, "");
    static_assert(MatrixGet<2, 2, BA>::value::value == 0, "");

    // #3 calc B*A*C
    typedef typename Multiply<BA, C>::result BAC;
    static_assert(BAC::size == 3, "");
    static_assert(ListGet<0, BAC>::value::size == 3, "");
    static_assert(ListGet<1, BAC>::value::size == 3, "");
    static_assert(ListGet<2, BAC>::value::size == 3, "");
    static_assert(MatrixGet<0, 0, BAC>::value::value == 40, "");
    static_assert(MatrixGet<0, 1, BAC>::value::value == 56, "");
    static_assert(MatrixGet<0, 2, BAC>::value::value == 36, "");
    static_assert(MatrixGet<1, 0, BAC>::value::value == 50, "");
    static_assert(MatrixGet<1, 1, BAC>::value::value == 70, "");
    static_assert(MatrixGet<1, 2, BAC>::value::value == 45, "");
    static_assert(MatrixGet<2, 0, BAC>::value::value == 0, "");
    static_assert(MatrixGet<2, 1, BAC>::value::value == 0, "");
    static_assert(MatrixGet<2, 2, BAC>::value::value == 0, "");

    // #4 calc C*B
    typedef typename Multiply<C, B>::result CB;
    static_assert(CB::size == 3, "");
    static_assert(ListGet<0, CB>::value::size == 1, "");
    static_assert(ListGet<1, CB>::value::size == 1, "");
    static_assert(ListGet<2, CB>::value::size == 1, "");
    static_assert(MatrixGet<0, 0, CB>::value::value == 14, "");
    static_assert(MatrixGet<1, 0, CB>::value::value == 0, "");
    static_assert(MatrixGet<2, 0, CB>::value::value == 32, "");

    // #5 calc A*C
    typedef typename Multiply<A, C>::result AC;
    static_assert(AC::size == 1, "");
    static_assert(ListGet<0, AC>::value::size == 3, "");
    static_assert(MatrixGet<0, 0, AC>::value::value == 10, "");
    static_assert(MatrixGet<0, 1, AC>::value::value == 14, "");
    static_assert(MatrixGet<0, 2, AC>::value::value == 9, "");

    cout<< "matMulEfratCheck:\tSuccess" << endl;
}


// ----------------------------------------------------------------------
// --------------------TESTS-THAT-FAIL-COMPILATION-----------------------
// ----------------------------------------------------------------------


//void matAddFAIL1() {
//    typedef List<
//            List<Int<1>, Int<2>, Int<0>>,
//            List<Int<0>, Int<1>, Int<0>>
//    > matrix1;
//    typedef List<
//            List<Int<7>, Int<6>, Int<0>>,
//            List<Int<0>, Int<7>, Int<0>>,
//            List<Int<8>, Int<0>, Int<3>>
//    > matrix2;
//    typedef typename Add<matrix1, matrix2>::value matrix3;
//}


//void matAddFAIL2() {
//    typedef List<
//            List<Int<1>, Int<2>, Int<0>>,
//            List<Int<0>, Int<1>, Int<0>>
//    > matrix1;
//    typedef List<
//            List<Int<7>, Int<6>>,
//            List<Int<0>, Int<7>>
//    > matrix2;
//    typedef typename Add<matrix1, matrix2>::value matrix3;
//}


//void matMulEfratFAIL1() {
//    typedef List<
//            List<Int<1>, Int<2>, Int<3>>
//    > A;
//    typedef List<
//            List<Int<4>>,
//            List<Int<5>>,
//            List<Int<0>>
//    > B;
//    typedef List<
//            List<Int<1>, Int<2>, Int<1>>,
//            List<Int<0>, Int<0>, Int<1>>,
//            List<Int<3>, Int<4>, Int<2>>
//    > C;
//    typedef typename Multiply<A, B>::result AB;
//    // calc A*B*C - should FAIL YOUR COMPILATION
//    typedef typename Multiply<AB, C>::result ABC;
//}


//void matMulEfratFAIL2() {
//    typedef List<
//            List<Int<1>, Int<2>, Int<3>>
//    > A;
//    typedef List<
//            List<Int<4>>,
//            List<Int<5>>,
//            List<Int<0>>
//    > B;
//    typedef List<
//            List<Int<1>, Int<2>, Int<1>>,
//            List<Int<0>, Int<0>, Int<1>>,
//            List<Int<3>, Int<4>, Int<2>>
//    > C;
//    // #2 calc C*A - should FAIL YOUR COMPILATION
//    typedef typename Multiply<C, A>::result CA;
//}

// ----------------------------------------------------------------------

int main() {
    listGetSetPrependCheck();
    matAddCheck();
    matMulPdfCheck();
    matMulCensorCheck();
    matMulEfratCheck();
    return 0;
}

