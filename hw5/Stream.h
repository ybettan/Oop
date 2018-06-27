#ifndef PART2_STREAM_H
#define PART2_STREAM_H

#include <algorithm>
#include <functional>
#include <vector>
#include <map>

//FIXME: remove when done
#include <iostream>
using std::cout;
using std::endl;

using std::function;
using std::vector;
using std::map;



//-----------------------------------------------------------------------------
//                                 Stream
//-----------------------------------------------------------------------------

template <typename T>
class Stream {

  private:

    typedef void (*ForEachFunc)(const T*);
    typedef T* (*ReduceFunc)(const T*, const T*);
    typedef bool (*Predicate)(const T*);

    vector<T*> elements;
    function<vector<T*>()> activationFunctions;


    /* private C'tor */
    Stream(vector<T*> v) :
        elements(v),
        activationFunctions([this]() {return elements;}) {}
    

    T* reduceAux(T *initial, ReduceFunc func, vector<T*>& vec, int vecSize) {

        if (vecSize == 1)
            return func(initial, vec[0]);

        return func(vec[vecSize-1], reduceAux(initial, func, vec, vecSize-1));
    }


  public:

    /* TContainer is a Collection<T*> */
    template <typename TContainer>
    static Stream<T> of(TContainer &container) {

        /* copy the container into a vector */
        vector<T*> ePtrVec(container.begin(), container.end());

        return Stream<T>(ePtrVec);
    }


    /* overload the method for map containers */
    template <typename K, typename V>
    static Stream<T> of(map<K,V> &mapContainer) {

        /* copy the mapContainer values into a vector */
        vector<T*> ePtrVec;
        for (auto iter=mapContainer.begin() ; iter!=mapContainer.end() ; iter++)
            ePtrVec.push_back(iter->second);

        return Stream<T>(ePtrVec);
    }


    template <typename TContainer>
    TContainer collect() {

        vector<T*> resVec = activationFunctions();
        TContainer resContainer(resVec.begin(), resVec.end());

        return resContainer;
    }


    void forEach(ForEachFunc func) {

        vector<T*> resVec = activationFunctions();
        for_each(resVec.begin(), resVec.end(), func);
    }


    T* reduce(T *initial, ReduceFunc func) {

        vector<T*> resVec = activationFunctions();
        return reduceAux(initial, func, resVec, resVec.size());
    }


    T* min() {

        vector<T*> resVec = activationFunctions();
        T *res = resVec[0];
        for (int i=1 ; i<resVec.size() ; i++) {
            if (*resVec[i] < *res)
                res = resVec[i];
        }

        return res;
    }


    T* max() {

        vector<T*> resVec = activationFunctions();
        T *res = resVec[0];
        for (int i=1 ; i<resVec.size() ; i++) {
            if (*resVec[i] > *res)
                res = resVec[i];
        }

        return res;
    }

    
    int count() {

        vector<T*> resVec = activationFunctions();
        return resVec.size();
    }


    bool anyMatch(Predicate pred) {

        vector<T*> resVec = activationFunctions();
        return std::any_of(resVec.begin(), resVec.end(), pred);
    }


    bool allMatch(Predicate pred) {

        vector<T*> resVec = activationFunctions();
        return std::all_of(resVec.begin(), resVec.end(), pred);
    }


    T* findFirst(Predicate pred) {

        vector<T*> resVec = activationFunctions();
        auto findRes = std::find_if(resVec.begin(), resVec.end(), pred);
        return (findRes == resVec.end()) ? nullptr : *findRes;
    }


    //FIXME: remove when done
    void printElements() {
        cout << "{";
        for (int i=0 ; i<elements.size() ; i++) {
            if (i != elements.size() - 1)
                cout << *elements[i] << ", ";
            else
                cout << *elements[i] << "}" << endl;
        } 
    }
};





#endif //PART2_STREAM_H
