#include <iostream>
#include <assert.h>
#include "MatrixOperations.h"

using namespace std;

int main() {

//-----------------------------------------------------------------------------
//                          test Transpost (to understand it)
//-----------------------------------------------------------------------------

    typedef List<List<Int<1>, Int<2>, Int<3>>> m13;
    typedef typename Transpose<m13>::matrix m31;

    static_assert(ListGet<0, m13>::value::size == 3, "Error");
    static_assert(ListGet<0, m31>::value::size == 1, "Error");

    static_assert(ListGet<0, ListGet<0, m13>::value>::value::value == 1, "Error");
    static_assert(ListGet<1, ListGet<0, m13>::value>::value::value == 2, "Error");
    static_assert(ListGet<2, ListGet<0, m13>::value>::value::value == 3, "Error");

    static_assert(ListGet<0, ListGet<0, m31>::value>::value::value == 1, "Error");
    static_assert(ListGet<0, ListGet<1, m31>::value>::value::value == 2, "Error");
    static_assert(ListGet<0, ListGet<2, m31>::value>::value::value == 3, "Error");


//-----------------------------------------------------------------------------
//                            test Add
//-----------------------------------------------------------------------------

    typedef List<
                    List<Int<1>, Int<2>, Int<3>>,
                    List<Int<4>, Int<5>, Int<6>>,
                    List<Int<7>, Int<8>, Int<9>>
                > m1;

    typedef List<
                    List<Int<1>, Int<1>, Int<1>>,
                    List<Int<1>, Int<1>, Int<1>>,
                    List<Int<1>, Int<1>, Int<1>>
                > m2;

    typedef typename Add<m1, m2>::result sumMatrix;

    static_assert(sumMatrix::size == 3, "Error");
    static_assert(sumMatrix::head::size == 3, "Error");

    static_assert(ListGet<0, typename ListGet<0, sumMatrix>::value>::value::value == 2, "Error");
    static_assert(ListGet<1, typename ListGet<0, sumMatrix>::value>::value::value == 3, "Error");
    static_assert(ListGet<2, typename ListGet<0, sumMatrix>::value>::value::value == 4, "Error");
    static_assert(ListGet<0, typename ListGet<1, sumMatrix>::value>::value::value == 5, "Error");
    static_assert(ListGet<1, typename ListGet<1, sumMatrix>::value>::value::value == 6, "Error");
    static_assert(ListGet<2, typename ListGet<1, sumMatrix>::value>::value::value == 7, "Error");
    static_assert(ListGet<0, typename ListGet<2, sumMatrix>::value>::value::value == 8, "Error");
    static_assert(ListGet<1, typename ListGet<2, sumMatrix>::value>::value::value == 9, "Error");
    static_assert(ListGet<2, typename ListGet<2, sumMatrix>::value>::value::value == 10, "Error");

    typedef List<
                    List<Int<1>, Int<2>, Int<3>>,
                    List<Int<7>, Int<8>, Int<9>>
                > m3;

    typedef List<
                    List<Int<1>, Int<1>, Int<1>>,
                    List<Int<1>, Int<1>, Int<1>>
                > m4;

    typedef typename Add<m3, m4>::result sumMatrix2;

    static_assert(sumMatrix2::size == 2, "Error");
    static_assert(sumMatrix2::head::size == 3, "Error");

    static_assert(ListGet<0, typename ListGet<0, sumMatrix2>::value>::value::value == 2, "Error");
    static_assert(ListGet<1, typename ListGet<0, sumMatrix2>::value>::value::value == 3, "Error");
    static_assert(ListGet<2, typename ListGet<0, sumMatrix2>::value>::value::value == 4, "Error");
    static_assert(ListGet<0, typename ListGet<1, sumMatrix2>::value>::value::value == 8, "Error");
    static_assert(ListGet<1, typename ListGet<1, sumMatrix2>::value>::value::value == 9, "Error");
    static_assert(ListGet<2, typename ListGet<1, sumMatrix2>::value>::value::value == 10, "Error");

    typedef List<
                    List<Int<1>, Int<3>>,
                    List<Int<4>, Int<6>>,
                    List<Int<7>, Int<9>>
                > m5;

    typedef List<
                    List<Int<1>, Int<1>>,
                    List<Int<1>, Int<1>>,
                    List<Int<1>, Int<1>>
                > m6;

    typedef typename Add<m5, m6>::result sumMatrix3;

    static_assert(sumMatrix3::size == 3, "Error");
    static_assert(sumMatrix3::head::size == 2, "Error");

    static_assert(ListGet<0, typename ListGet<0, sumMatrix3>::value>::value::value == 2, "Error");
    static_assert(ListGet<1, typename ListGet<0, sumMatrix3>::value>::value::value == 4, "Error");
    static_assert(ListGet<0, typename ListGet<1, sumMatrix3>::value>::value::value == 5, "Error");
    static_assert(ListGet<1, typename ListGet<1, sumMatrix3>::value>::value::value == 7, "Error");
    static_assert(ListGet<0, typename ListGet<2, sumMatrix3>::value>::value::value == 8, "Error");
    static_assert(ListGet<1, typename ListGet<2, sumMatrix3>::value>::value::value == 10, "Error");

    typedef List<
                    List<Int<7>>
                > m7;

    typedef List<
                    List<Int<1>>
                > m8;

    typedef typename Add<m8, m7>::result sumMatrix4;

    static_assert(sumMatrix4::size == 1, "Error");
    static_assert(sumMatrix4::head::size == 1, "Error");

    static_assert(ListGet<0, typename ListGet<0, sumMatrix4>::value>::value::value == 8, "Error");

//-----------------------------------------------------------------------------
//                          test Multiply
//-----------------------------------------------------------------------------

    typedef List<
                    List<Int<1>, Int<2>, Int<3>, Int<4>>,
                    List<Int<5>, Int<6>, Int<7>, Int<8>>,
                    List<Int<9>, Int<10>, Int<11>, Int<12>>
                > m34;

    typedef List<
                    List<Int<2>, Int<1>>,
                    List<Int<2>, Int<1>>,
                    List<Int<2>, Int<1>>,
                    List<Int<2>, Int<1>>
                > m42;

    typedef typename Multiply<m34, m42>::result mulMatrix;

    static_assert(mulMatrix::size == 3, "Error");
    static_assert(mulMatrix::head::size == 2, "Error");

    static_assert(MatrixGet<0, 0, mulMatrix>::value::value == 20, "Error");
    static_assert(MatrixGet<0, 1, mulMatrix>::value::value == 10, "Error");
    static_assert(MatrixGet<1, 0, mulMatrix>::value::value == 52, "Error");
    static_assert(MatrixGet<1, 1, mulMatrix>::value::value == 26, "Error");
    static_assert(MatrixGet<2, 0, mulMatrix>::value::value == 84, "Error");
    static_assert(MatrixGet<2, 1, mulMatrix>::value::value == 42, "Error");

    typedef List<
                    List<Int<2>>
                > m11;

    typedef List<
                    List<Int<4>>
                > m11_2;

    typedef typename Multiply<m11, m11_2>::result mulMatrix2;

    static_assert(mulMatrix2::size == 1, "Error");
    static_assert(mulMatrix2::head::size == 1, "Error");

    static_assert(MatrixGet<0, 0, mulMatrix2>::value::value == 8, "Error");

    typedef List<
                    List<Int<1>, Int<2>, Int<3>>
                > m13;

    typedef List<
                    List<Int<2>, Int<1>>,
                    List<Int<2>, Int<1>>,
                    List<Int<2>, Int<1>>
                > m32;

    typedef typename Multiply<m13, m32>::result mulMatrix3;

    static_assert(mulMatrix3::size == 1, "Error");
    static_assert(mulMatrix3::head::size == 2, "Error");

    static_assert(MatrixGet<0, 0, mulMatrix3>::value::value == 12, "Error");
    static_assert(MatrixGet<0, 1, mulMatrix3>::value::value == 6, "Error");

    typedef List<
                    List<Int<4>>,
                    List<Int<8>>,
                    List<Int<12>>
                > m31_2;

    typedef List<
                    List<Int<2>, Int<1>>
                > m12;

    typedef typename Multiply<m31_2, m12>::result mulMatrix4;

    static_assert(mulMatrix4::size == 3, "Error");
    static_assert(mulMatrix4::head::size == 2, "Error");

    static_assert(MatrixGet<0, 0, mulMatrix4>::value::value == 8, "Error");
    static_assert(MatrixGet<0, 1, mulMatrix4>::value::value == 4, "Error");
    static_assert(MatrixGet<1, 0, mulMatrix4>::value::value == 16, "Error");
    static_assert(MatrixGet<1, 1, mulMatrix4>::value::value == 8, "Error");
    static_assert(MatrixGet<2, 0, mulMatrix4>::value::value == 24, "Error");
    static_assert(MatrixGet<2, 1, mulMatrix4>::value::value == 12, "Error");

    typedef List<
                    List<Int<1>, Int<2>>,
                    List<Int<5>, Int<6>>,
                    List<Int<9>, Int<10>>
                > m32_2;

    typedef List<
                    List<Int<2>>,
                    List<Int<2>>
                > m21;

    typedef typename Multiply<m32_2, m21>::result mulMatrix5;

    static_assert(mulMatrix5::size == 3, "Error");
    static_assert(mulMatrix5::head::size == 1, "Error");

    static_assert(MatrixGet<0, 0, mulMatrix5>::value::value == 6, "Error");
    static_assert(MatrixGet<1, 0, mulMatrix5>::value::value == 22, "Error");
    static_assert(MatrixGet<2, 0, mulMatrix5>::value::value == 38, "Error");

    typedef List<
                    List<Int<1>, Int<2>>
                > m12_2;

    typedef List<
                    List<Int<2>, Int<3>, Int<1>>,
                    List<Int<2>, Int<3>, Int<1>>
                > m23;

    typedef typename Multiply<m12_2, m23>::result mulMatrix6;

    static_assert(mulMatrix6::size == 1, "Error");
    static_assert(mulMatrix6::head::size == 3, "Error");

    static_assert(MatrixGet<0, 0, mulMatrix6>::value::value == 6, "Error");
    static_assert(MatrixGet<0, 1, mulMatrix6>::value::value == 9, "Error");
    static_assert(MatrixGet<0, 2, mulMatrix6>::value::value == 3, "Error");

    typedef List<
                    List<Int<1>, Int<2>>,
                    List<Int<5>, Int<6>>
                > m22;

    typedef List<
                    List<Int<2>, Int<1>>,
                    List<Int<2>, Int<1>>
                > m22_2;

    typedef typename Multiply<m22, m22_2>::result mulMatrix7;

    static_assert(mulMatrix7::size == 2, "Error");
    static_assert(mulMatrix7::head::size == 2, "Error");

    static_assert(MatrixGet<0, 0, mulMatrix7>::value::value == 6, "Error");
    static_assert(MatrixGet<0, 1, mulMatrix7>::value::value == 3, "Error");
    static_assert(MatrixGet<1, 0, mulMatrix7>::value::value == 22, "Error");
    static_assert(MatrixGet<1, 1, mulMatrix7>::value::value == 11, "Error");
}




















