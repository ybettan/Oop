#include <iostream>
#include "MatrixOperations.h"

int main() {
    typedef List<Int<1>, Int<2>, Int<3>> list1;
	static_assert(list1::head::value == 1, "Failed"); // = Int<1>
	typedef typename list1::next list1Tail; // = List<Int<2>, Int<3>>
	static_assert(list1::size == 3, "Failed"); // = 3
	static_assert(list1Tail::size == 2, "Failed"); // = 2
	
	typedef List<Int<1>, Int<2>, Int<3>> list2;
	typedef typename PrependList<Int<4>, list2>::list newList2; // = List< Int<4>, Int<1>, Int<2>, Int<3>>
	static_assert(newList2::head::value == 4, "Failed");
	
	typedef List<Int<1>, Int<2>, Int<3>> list3;
	static_assert(ListGet<0, list3>::value::value == 1, "Failed"); // = Int<1>
	static_assert(ListGet<2, list3>::value::value == 3, "Failed"); // = Int<3>
	
	typedef List<
                List< Int<1>, Int<2>, Int<0> >,
                List< Int<0>, Int<1>, Int<0> >,
                List< Int<0>, Int<0>, Int<5> >
            > matrix11;
            
	typedef List<
					List< Int<7>, Int<6>, Int<0> >,
					List< Int<0>, Int<7>, Int<0> >,
					List< Int<8>, Int<0>, Int<3> >
				> matrix21;
				
	typedef typename Add<matrix11, matrix21>::result matrix31; // = List<
															   //			List< Int<8>, Int<8>, Int<0> >,
															   //			List< Int<0>, Int<8>, Int<0> >,
															   //			List< Int<8>, Int<0>, Int<8> >
															   //		 >
	static_assert(matrix31::head::head::value == 8, "Failed");
	
	typedef List<
                List< Int<1>, Int<2> >,
                List< Int<0>, Int<1> >
            > matrix12;
            
	typedef List<
					List< Int<0>, Int<7> >,
					List< Int<8>, Int<0> >
				> matrix22;
				
	typedef typename Multiply<matrix12, matrix22>::result matrix32; // = List<
																    //			List< Int<16>, Int<7> >,
																    //			List< Int<8>, Int<0> >
																    //		  >
																 
	static_assert(matrix32::head::head::value == 16, "Failed");
}