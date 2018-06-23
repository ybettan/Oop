#ifndef ADD_MULTIPLY_MATRIXES_H
#define ADD_MULTIPLY_MATRIXES_H

#include "Utilities.h"
#include "Transpose.h"


//-----------------------------------------------------------------------------
//                                   Add
//-----------------------------------------------------------------------------

/* AddInt */
template <typename T1, typename T2>
struct AddInt {
    typedef Int<T1::value + T2::value> result;
};



/* AddAux */
template <typename M1, typename M2, int row, int col>
struct AddAux {
  private:
    typedef typename AddAux<M1, M2, row, col - 1>::result tmp;
    typedef typename AddInt<typename MatrixGet<row, col, M1>::value,
                            typename MatrixGet<row, col ,M2>::value>::result newElement;
  public:
    typedef typename MatrixSet<row, col, newElement, tmp>::matrix result;
};

template <typename M1, typename M2, int row>
struct AddAux<M1, M2, row, 0> {
  private:
    typedef typename AddAux<M1, M2, row - 1, M1::head::size - 1>::result tmp;
    typedef typename AddInt<typename MatrixGet<row, 0, M1>::value,
                            typename MatrixGet<row, 0, M2>::value>::result newElement;
  public:
    typedef typename MatrixSet<row, 0, newElement, tmp>::matrix result;
};

template <typename M1, typename M2>
struct AddAux<M1, M2, 0, 0> {
  private:
    typedef typename AddInt<typename MatrixGet<0, 0, M1>::value,
                            typename MatrixGet<0, 0 ,M2>::value>::result newElement;
  public:
    typedef typename MatrixSet<0, 0, newElement, M2>::matrix result;
};



/* Add */
template <typename M1, typename M2>
struct Add {
    static_assert(M1::size == M2::size);
    static_assert(M1::head::size == M2::head::size);
  private:
    constexpr static int numRow = M1::size;
    constexpr static int numCol = M1::head::size;
  public:
    typedef typename AddAux<M1, M2, numRow - 1, numCol - 1>::result result;
};


//-----------------------------------------------------------------------------
//                                Multiply
//-----------------------------------------------------------------------------

/* CreateListOf */
template <typename T, int size>
struct CreateListOf {
  private:
    typedef typename CreateListOf<T, size - 1>::list tmp;
  public:
    typedef typename PrependList<T, tmp>::list list;
};

template <typename T>
struct CreateListOf<T, 1> {
    typedef List<T> list;
};



/* CreateZeroMatrix */
template <int numRow, int numCol>
struct CreateZeroMatrix {
  private:
    typedef typename CreateListOf<Int<0>, numCol>::list singleRow;
  public:
    typedef typename CreateListOf<singleRow, numRow>::list matrix;
};



/* MultiplyInt */
template <typename T1, typename T2>
struct MultiplyInt {
    typedef Int<T1::value * T2::value> result;
};



/* MultiplyIntList - the result is of type Int<> */
template <typename L1, typename L2>
struct MultiplyIntList {
  private:
    typedef typename MultiplyIntList<typename L1::next, typename L2::next>::result tmp;
    typedef typename MultiplyInt<typename L1::head, typename L2::head>::result newElement;
  public:
    typedef typename AddInt<newElement, tmp>::result result;
};

template <typename T1, typename T2>
struct MultiplyIntList<List<T1>, List<T2>> {
    typedef typename MultiplyInt<T1, T2>::result result;
};



/* MultiplyAux */
template <typename M1, typename M2T, int row, int col>
struct MultiplyAux {
  private:
    typedef typename MultiplyAux<M1, M2T, row, col - 1>::result tmp;
    typedef typename MultiplyIntList<typename ListGet<row, M1>::value,
                                     typename ListGet<col, M2T>::value>::result newElement;
  public:
    typedef typename MatrixSet<row, col, newElement, tmp>::matrix result;
};

template <typename M1, typename M2T, int row>
struct MultiplyAux<M1, M2T, row, 0> {
  private:
    typedef typename MultiplyAux<M1, M2T, row - 1, M2T::size - 1>::result tmp;
    typedef typename MultiplyIntList<typename ListGet<row, M1>::value,
                                     typename ListGet<0, M2T>::value>::result newElement;
  public:
    typedef typename MatrixSet<row, 0, newElement, tmp>::matrix result;
};

template <typename M1, typename M2T>
struct MultiplyAux<M1, M2T, 0, 0> {
  private:
    constexpr static int numRow = M1::size;
    constexpr static int numCol = M2T::size;
    typedef typename CreateZeroMatrix<numRow, numCol>::matrix tmp;
    typedef typename MultiplyIntList<typename ListGet<0, M1>::value,
                                     typename ListGet<0, M2T>::value>::result newElement;
  public:
    typedef typename MatrixSet<0, 0, newElement, tmp>::matrix result;
};



/* Multiply */
template <typename M1, typename M2>
struct Multiply {
    static_assert(M1::head::size == M2::size);
  private:
    constexpr static int numRow = M1::size;
    constexpr static int numCol = M2::head::size;
    typedef typename Transpose<M2>::matrix M2T;
  public:
    typedef typename MultiplyAux<M1, M2T, numRow - 1, numCol - 1>::result result;
};





#endif //ADD_MULTIPLY_MATRIXES_H







