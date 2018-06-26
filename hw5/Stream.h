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
    vector<T*> elements;
    function<vector<T*>()> activationFunctions;


    /* private C'tor */
    Stream(vector<T*> v) :
        elements(v),
        activationFunctions([this]() {return elements;}) {}


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
