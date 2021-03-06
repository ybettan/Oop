#!/bin/bash

echo ---------------------------------------------------------
echo "                  Part1Examples.cpp"
echo ---------------------------------------------------------
g++ -std=c++11 Part1Examples.cpp
./a.out

echo ---------------------------------------------------------
echo "                  AviramTest.cpp"
echo ---------------------------------------------------------
g++ -std=c++11 AviramTest.cpp
./a.out

echo ---------------------------------------------------------
echo "             Part1TestUtilities.cpp"
echo ---------------------------------------------------------
g++ -std=c++11 Part1TestUtilities.cpp
./a.out

echo ---------------------------------------------------------
echo "             Part1TestAddMultiplyMatrixes.cpp"
echo ---------------------------------------------------------
g++ -std=c++11 Part1TestAddMultiplyMatrixes.cpp
./a.out

echo ---------------------------------------------------------
echo "             Part2Examples.cpp"
echo ---------------------------------------------------------
g++ -std=c++11 Part2Examples.cpp
./a.out

echo ---------------------------------------------------------
echo "             StreamTest.cpp"
echo ---------------------------------------------------------
g++ -std=c++11 StreamTest.cpp
./a.out

echo ---------------------------------------------------------
echo "             Guy Tests"
echo ---------------------------------------------------------
cd oop-hw5-tests
make
cd ../

echo ---------------------------------------------------------
echo "             EranTest.cpp"
echo ---------------------------------------------------------
g++ -std=c++11 EranTest.cpp
./a.out

echo ---------------------------------------------------------
echo "                    Hadas Tests"
echo ---------------------------------------------------------
cd OOP5_Tests
cd part1
./OOP5_Part1_RunTest.sh
cd ../part2
./OOP5_Part2_RunTest.sh
cd ../../

echo ---------------------------------------------------------
echo "                    StreamComplexTest"
echo ---------------------------------------------------------
g++ -std=c++11 StreamComplexTest.cpp
./a.out

rm a.out





