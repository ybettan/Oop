#ifndef UTILITIES_H
#define UTILITIES_H



//-----------------------------------------------------------------------------
//                                 List
//-----------------------------------------------------------------------------

/* an empty list support only size */
template <typename ...TT>
struct List {
    constexpr static int size = sizeof...(TT);
};

template <typename T, typename ...TT>
struct List<T, TT...> {
    typedef T head;
    typedef List<TT...> next;
    constexpr static int size = sizeof...(TT) + 1;
};


//-----------------------------------------------------------------------------
//                             PrependList
//-----------------------------------------------------------------------------

template <typename, typename>
struct PrependList;

template <typename T, typename ...TT>
struct PrependList<T, List<TT...>> {
    typedef List<T, TT...> list;
};


//-----------------------------------------------------------------------------
//                                ListGet
//-----------------------------------------------------------------------------

template <int, typename ...>
struct ListGet;

template <int N, typename T, typename ...TT>
struct ListGet<N, List<T, TT...>> {
    typedef typename ListGet<N-1, List<TT...>>::value value;
};

/* stoppping condition.
 * in addition, if the list is of size=1 then the only valid index is 0 */
template <typename T, typename ...TT>
struct ListGet<0, List<T, TT...>> {
    typedef T value;
};


//-----------------------------------------------------------------------------
//                                ListSet
//-----------------------------------------------------------------------------

template <int, typename, typename...>
struct ListSet;

template <int N, typename T, typename ListHead, typename ...ListTail>
struct ListSet<N, T, List<ListHead, ListTail...>> {
    typedef typename PrependList<
        ListHead, typename ListSet<N-1, T, List<ListTail...>>::list
        >::list list;
};

/* stoppping condition.
 * in addition, if the list is of size=1 then the only valid index is 0 */
template <typename T, typename ListHead, typename ...ListTail>
struct ListSet<0, T, List<ListHead, ListTail...>> {
    typedef typename PrependList<T, List<ListTail...>>::list list;
};

//-----------------------------------------------------------------------------
//                                MatrixGet
//-----------------------------------------------------------------------------

template <int row, int col, typename M>
struct MatrixGet {
    typedef typename ListGet<col, typename ListGet<row, M>::value>::value value;
};

//-----------------------------------------------------------------------------
//                                MatrixSet
//-----------------------------------------------------------------------------

template <int row, int col, typename T, typename M>
struct MatrixSet {
  private:
      typedef typename ListGet<row, M>::value OriginalRow;
      typedef typename ListSet<col, T, OriginalRow>::list NewRow;
  public:
      typedef typename ListSet<row, NewRow, M>::list matrix;
};

//-----------------------------------------------------------------------------
//                                   Int
//-----------------------------------------------------------------------------

template <int N>
struct Int {
    constexpr static int value = N;
};






#endif //UTILITIES_H







