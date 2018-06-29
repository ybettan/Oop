//
// Created by מיכאל on 21/06/2018.
//

#ifndef OOP5_TESTUTILITIES_H
#define OOP5_TESTUTILITIES_H
template<typename Type1, typename Type2>
struct SameType;

template<typename T, typename U>
struct SameType {
    constexpr static bool result = false;
};

template<typename T>
struct SameType<T, T> {
    constexpr static bool result = true;
};

#endif //OOP5_TESTUTILITIES_H
