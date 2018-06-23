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
//                                   Int
//-----------------------------------------------------------------------------

template <int N>
struct Int {
    constexpr static int value = N;
};


//-----------------------------------------------------------------------------
//                                   Add
//-----------------------------------------------------------------------------

/* AddInt */
template <typename T1, typename T2>
struct AddInt {
    typedef Int<T1::value + T2::value> result;
};

/* AddList */
template <typename L1, typename L2>
struct AddList {
    static_assert(L1::size == L2::size);
    typedef typename PrependList<
        typename AddInt<typename L1::head, typename L2::head>::result,
        typename AddList<typename L1::next, typename L2::next>::result
        >::list result;
};

template <typename T1, typename T2>
struct AddList<List<T1>, List<T2>> {
    typedef List<typename AddInt<T1, T2>::result> result;
};

/* Add */
template <typename M1, typename M2>
struct Add {
    static_assert(M1::size == M2::size);
    typedef typename PrependList<
        typename AddList<typename M1::head, typename M2::head>::result,
        typename Add<typename M1::next, typename M2::next>::result
        >::list result;
};

template <typename L1, typename L2>
struct Add<List<L1>, List<L2>> {
    typedef List<typename AddList<L1, L2>::result> result;
};


#endif //UTILITIES_H







