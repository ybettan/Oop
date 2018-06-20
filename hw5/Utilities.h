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

/* an empty list support only size, FIXME: realy */
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





#endif //UTILITIES_H
