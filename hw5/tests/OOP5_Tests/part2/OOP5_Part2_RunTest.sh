#!/bin/bash

echo "ExampleTest - from webcourse"
res=`g++ -std=c++11 -g Part2Examples.cpp Stream.h -o ExampleTest.out 2>&1`;
if [ "${res}" != "" ]
then
	echo "Compilation Failed "
	echo "${res}"
else
	./ExampleTest.out
fi
echo ""
echo ""

echo "Part2_OurTestFile"
export LD_LIBRARY_PATH=/usr/local/lib64:$LD_LIBRARY_PATH #cant compile without due missing libary
res=`g++ -std=c++11 -g Part2_OurTestFile.cpp Stream.h -o Part2_OurTestFile.out 2>&1`;
if [ "${res}" != "" ]
then
	echo "Compilation Failed "
	echo "${res}"
else
    ./Part2_OurTestFile.out
fi
