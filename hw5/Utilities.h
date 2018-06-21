#ifndef UTILITIES_H
#define UTILITIES_H


//-----------------------------------------------------------------------------
//                                   Int
//-----------------------------------------------------------------------------

template <int N>
struct Int {
    constexpr static int value = N;
};


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

//FIXME: can we assume an empty list won't be an argument? 
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


#endif //UTILITIES_H







