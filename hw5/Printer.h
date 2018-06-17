#ifndef PART2_PRINTER_H
#define PART2_PRINTER_H

#include "Utilities.h"
#include <iostream>

template<typename>
struct Printer;

template<typename Head, typename... Tail>
struct Printer<List<Head, Tail...>>{
    static void print(std::ostream& output){
        Printer<Head>::print(output);
        Printer<List<Tail...>>::print(output);
    }
};

template<>
struct Printer<List<>>{
    static void print(std::ostream& output){
        output << std::endl;
    }
};

template<int N>
struct Printer<Int<N>>{
    static void print(std::ostream& output){
        output << N << " ";
    }
};

#endif //PART2_PRINTER_H
