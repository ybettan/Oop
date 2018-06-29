#!/bin/bash

echo "TestInt"
res=`g++ -std=c++11 -g TestInt.cpp 2>&1`;
if [ "${res}" != "" ]
then
	echo "Failed"
else
	echo "Passed"
fi 

echo "TestList";
res=`g++ -std=c++11 -g TestList.cpp 2>&1`;
if [ "${res}" != "" ]
then
	echo "Failed"
else
	echo "Passed"
fi 

echo "TestMatrix_Add"
res=`g++ -std=c++11 -g TestMatrix_Add.cpp 2>&1`;
if [ "${res}" != "" ]
then
	echo "Failed"
else
	echo "Passed"
fi 

echo "TestMatrix_Multiply"
res=`g++ -std=c++11 -g TestMatrix_Multiply.cpp 2>&1`;
if [ "${res}" != "" ]
then
	echo "Failed"
else
	echo "Passed"
fi

#ExamplesFromWwebcourse:
echo "Part1Examples"
res=`g++ -std=c++11 -g Part1Examples.cpp 2>&1`
if [ "${res}" != "" ]
then
	echo "Failed"
else
	echo "Passed"
fi


#Compilation should fail 
echo "TestCompilationFailAdd"
res=`g++ -std=c++11 -g TestCompilationFailAdd.cpp  2>&1`
if [ "${res}" == "" ]
then
	echo "Failed"
else
	echo "Passed"
fi

#Compilation should fail 
echo "TestCompilationFailMultiply"
res=`g++ -std=c++11 -g TestCompilationFailMultiply.cpp 2>&1`
if [ "${res}" == "" ]
then
	echo "Failed"
else
	echo "Passed"
fi 

