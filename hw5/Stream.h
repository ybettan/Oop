#ifndef PART2_STREAM_H
#define PART2_STREAM_H

#include <algorithm>
#include <functional>
#include <vector>
#include <map>

using std::function;
using std::vector;
using std::map;



//-----------------------------------------------------------------------------
//                                 Stream
//-----------------------------------------------------------------------------

template <typename T>
class Stream {

  private:

    function<vector<T*>()> activationFunc;


    T* reduceAux(T *initial, function<T*(const T*, const T*)> func,
            vector<T*>& vec, int vecSize) {

        if (vecSize == 1)
            return func(initial, vec[0]);

        return func(vec[vecSize-1], reduceAux(initial, func, vec, vecSize-1));
    }

    bool contain(vector<T*>& vec, T *t, function<bool(const T*, const T*)> comp) {

        for (T *e : vec) {
            if (comp(e, t))
                return true;
        }
        return false;
    }


  public:

    /* public C'tor */
    Stream(function<vector<T*>()> activationFunc) : activationFunc(activationFunc) {}


    /* TContainer is a Collection<T*> */
    template <typename TContainer>
    static Stream<T> of(TContainer &container) {

        /* copy the container into a vector */
        vector<T*> ePtrVec(container.begin(), container.end());

        /* create the first function that simply reutrns the initial vector */
        function<vector<T*>()> initialActivationFunc = [ePtrVec]() {return ePtrVec;};

        return Stream<T>(initialActivationFunc);
    }


    /* overload the method for map containers */
    template <typename K, typename V>
    static Stream<T> of(map<K,V> &mapContainer) {

        /* copy the mapContainer values into a vector */
        vector<T*> ePtrVec;
        for (auto iter=mapContainer.begin() ; iter!=mapContainer.end() ; iter++)
            ePtrVec.push_back(iter->second);

        /* create the first function that simply reutrns the initial vector */
        function<vector<T*>()> initialActivationFunc = [ePtrVec]() {return ePtrVec;};

        return Stream<T>(initialActivationFunc);
    }


    Stream<T>& filter(function<bool(const T*)> pred) {

        /* keep the old activation function */
        function<vector<T*>()> oldActivationFunc = activationFunc;

        /* save the new activation function */
        activationFunc = [oldActivationFunc, pred]() {

            /* when activated the function first activate all previous actions */
            vector<T*> updatedElements = oldActivationFunc();

            /* filter the vector */
            vector<T*> tmp(updatedElements.size());
            auto lastIter = std::copy_if(updatedElements.begin(),
                    updatedElements.end(), tmp.begin(), pred);
            tmp.erase(lastIter, tmp.end());
            updatedElements = tmp;
            return updatedElements;
        };

        return *this;
    }


    template <typename R>
    Stream<R> map(function<R*(const T*)> mapFunc) {

        /* keep the old activation function */
        function<vector<T*>()> oldActivationFunc = activationFunc;

        /* create the new activation function */
        function<vector<R*>()> newActivationFunc =
            [oldActivationFunc, mapFunc]() {

                /* when activated the function first activate all previous actions */
                vector<T*> updatedElements = oldActivationFunc();

                /* create a new vector to hold mapping result */
                vector<R*> newUpdatedElements = vector<R*>(updatedElements.size());
                std::transform(updatedElements.begin(), updatedElements.end(),
                        newUpdatedElements.begin(), mapFunc);

                return newUpdatedElements;
            };

        return Stream<R>(newActivationFunc);
    }


    Stream<T>& distinct(function<bool(const T*, const T*)> comp) {

        /* keep the old activation function */
        function<vector<T*>()> oldActivationFunc = activationFunc;

        /* save the new activation function.
         * this is captured for contain method */
        activationFunc = [this, oldActivationFunc, comp]() {

            /* when activated the function first activate all previous actions */
            vector<T*> updatedElements = oldActivationFunc();

            /* distinct the vector */
            vector<T*> distinctVec;
            for (T *e : updatedElements) {
                if(!contain(distinctVec, e, comp))
                    distinctVec.push_back(e);
            }
            updatedElements = distinctVec;

            return updatedElements;
        };

        return *this;
    }


    /* distinct with the default operator== of T (not T*).
     * ASSUMPTION: T implements operator== */
    Stream<T>& distinct() {

        return distinct([](const T *t1, const T *t2) {return *t1 == *t2;});
    }
    

    Stream<T>& sorted(function<bool(const T*, const T*)> isFirstSmaller) {

        /* keep the old activation function */
        function<vector<T*>()> oldActivationFunc = activationFunc;

        /* save the new activation function */
        activationFunc = [oldActivationFunc, isFirstSmaller]() {

            /* when activated the function first activate all previous actions */
            vector<T*> updatedElements = oldActivationFunc();

            /* sort the vector */
            std::sort(updatedElements.begin(), updatedElements.end(), isFirstSmaller);

            return updatedElements;
        };

        return *this;
    }


    /* sorted with the default operator< of T (not T*).
     * ASSUMPTION: T implements operator< */
    Stream<T>& sorted() {

        return sorted([](const T *t1, const T *t2) {return *t1 < *t2;});
    }


    template <typename TContainer>
    TContainer collect() {

        vector<T*> resVec = activationFunc();
        TContainer resContainer(resVec.begin(), resVec.end());

        return resContainer;
    }


    void forEach(function<void(T*)> func) {

        vector<T*> resVec = activationFunc();
        for_each(resVec.begin(), resVec.end(), func);
    }


    T* reduce(T *initial, function<T*(const T*, const T*)> func) {

        vector<T*> resVec = activationFunc();
        return reduceAux(initial, func, resVec, resVec.size());
    }


    T* min() {

        vector<T*> resVec = activationFunc();
        T *res = resVec[0];
        for (int i=1 ; i<resVec.size() ; i++) {
            if (*resVec[i] < *res)
                res = resVec[i];
        }

        return res;
    }


    T* max() {

        vector<T*> resVec = activationFunc();
        T *res = resVec[0];
        for (int i=1 ; i<resVec.size() ; i++) {
            if (!(*resVec[i] < *res) && !(*resVec[i] == *res))
                res = resVec[i];
        }

        return res;
    }

    
    int count() {

        vector<T*> resVec = activationFunc();
        return resVec.size();
    }


    bool anyMatch(function<bool(const T*)> pred) {

        vector<T*> resVec = activationFunc();
        return std::any_of(resVec.begin(), resVec.end(), pred);
    }


    bool allMatch(function<bool(const T*)> pred) {

        vector<T*> resVec = activationFunc();
        return std::all_of(resVec.begin(), resVec.end(), pred);
    }


    T* findFirst(function<bool(const T*)> pred) {

        vector<T*> resVec = activationFunc();
        auto findRes = std::find_if(resVec.begin(), resVec.end(), pred);
        return (findRes == resVec.end()) ? nullptr : *findRes;
    }

};





#endif //PART2_STREAM_H
