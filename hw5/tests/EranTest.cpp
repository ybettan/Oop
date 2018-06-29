#include <ostream>
#include <iostream>
#include <cassert>
#include <string>
#include <list>
#include "Stream.h"

using namespace std;

template<typename R,typename T>
class Pair{
    private:
        R x;
        T y;

    public:
        Pair(R x, T y):x(x),y(y){   };

        bool operator==(const Pair& pair) const{
            return (pair.x==x && pair.y==y);
        }

        bool operator<(const Pair &rhs) const {
            if (x < rhs.x)
                return true;
            if (rhs.x < x)
                return false;
            return y < rhs.y;
        }

    R getX() const {
        return x;
    }

    T getY() const {
        return y;
    }

};


class TestClass{
    private:
        std::map<int,Pair<string,string>*> map;
        std::list<Pair<int,double>*> list;
        std::set<Pair<string,string>*> set;

    public:
        TestClass(){
            map[1]=new Pair<string,string>("a","abc");
            map[2]=new Pair<string,string>("ab","aa");
            map[3]=new Pair<string,string>("cba","cab");
            map[4]=new Pair<string,string>("aaa","aa");
            map[5]=new Pair<string,string>("bb","aa");
            map[6]=new Pair<string,string>("ab","aa");
            map[7]=new Pair<string,string>("ab","aa");

            list.push_back(new Pair<int, double>(1,3.1));
            list.push_back(new Pair<int, double>(3,3));
            list.push_back(new Pair<int, double>(4,2.8));
            list.push_back(new Pair<int, double>(6,7.1));
            list.push_back(new Pair<int, double>(1,1));

            set.insert(new Pair<string,string>("1","1"));
            set.insert(new Pair<string,string>("4","3"));
            set.insert(new Pair<string,string>("3","3"));
            set.insert(new Pair<string,string>("34","345"));
            set.insert(new Pair<string,string>("50","2"));
        }

        void testCreation(){
            assert((Stream<Pair<string,string>>::of(map).count()) == 7);
            assert((Stream<Pair<int,double>>::of(list).count()) == 5);
            assert((Stream<Pair<string,string>>::of(set).count()) == 5);

            std::cout<<"creation-successful\n";
        }

        void testFilter(){
            assert((Stream<Pair<string,string>>::of(map).filter([](const Pair<string,string>* mem){return mem->getX()<mem->getY();}).count())==1);
            assert((Stream<Pair<int,double >>::of(list).filter([](const Pair<int,double>* mem){return mem->getX()+mem->getY()>5;}).count())==3);
            auto result=Stream<Pair<string,string>>::of(set).filter([](const Pair<string,string>* mem){return mem->getY()==mem->getX();}).collect<std::set<Pair<string,string>*>>();

            assert(result.size() == 2);

            std::cout<<"Filter-successful\n";

        }

        void testMap(){
            auto result=Stream<Pair<string,string>>::of(map).map<int>([](const Pair<string,string>* mem){ return new int(mem->getY().size());}).collect<std::list<int*>>();

            int arr[7]={3,2,3,2,2,2,2};

            int idx=0;
            for(auto& mem:result){
                assert(*mem==arr[idx]);
                idx++;
            }

            std::cout<<"Map-successful\n";
        }

        void testDistinct(){
            assert((Stream<Pair<string,string>>::of(map).distinct().count())==5);
            assert((Stream<Pair<int,double>>::of(list).distinct().count())==5);
            assert((Stream<Pair<string,string>>::of(set).distinct([](const Pair<string,string> *t,const Pair<string,string>* h){return t->getX()>t->getY() && h->getX()>h->getY();})).count()==4);

            std::cout<<"Distinct-successful\n";
        }

        void testSorted(){
            auto result1=Stream<Pair<int,double>>::of(list).sorted().collect<std::vector<Pair<int,double>*>>();
            auto result2=Stream<Pair<string,string>>::of(set).sorted([](const Pair<string,string> *t,const Pair<string,string>* h){ return t->getX().size()+t->getY().size()<h->getY().size()+h->getX().size() ;}).collect<std::vector<Pair<string,string>*>>();

            double arr[5]={1,3.1,3,2.8,7.1};

            int idx=0;
            for(auto& mem : result1){
                assert(mem->getY()==arr[idx]);
                idx++;
            }

            string arr2[]={"1","4","3","50","34"};

            idx=0;

            for(auto& mem : result2){
                assert(mem->getX()==arr2[idx]);
                idx++;
            }

            std::cout<<"Sorted-successful\n";
        }

        void testForEach(){
            double sum=0;
            Stream<Pair<int,double>>::of(list).forEach([&sum](const Pair<int,double>* t){sum+=t->getY()+t->getX();});

            assert(sum == 32);

            std::cout<<"forEach-successful\n";
        }

        void testReduce(){
            Pair<int,double> sum(0,0);
            auto result=Stream<Pair<int,double>>::of(list).
                           reduce(&sum,[](const Pair<int,double>* t1,const Pair<int, double>* t2){ return new Pair<int,double>(t1->getX()+t2->getX(),t1->getY()+t2->getY());});

            assert(result->getY()+result->getX()==32);

            std::cout<<"reduce-successful\n";

        }

        void testMin(){
            auto result=Stream<Pair<int,double>>::of(list).min();

            assert(result->getX()==1 && result->getY()==1);

            auto result1=Stream<Pair<string,string>>::of(set).min();

            assert(result1->getX()=="1" && result1->getY()=="1");

            std::cout<<"min-successful\n";

        }

        void testMax(){
            auto result=Stream<Pair<int,double>>::of(list).max();

            assert(result->getX()==6 && result->getY()==7.1);

            auto result1=Stream<Pair<string,string>>::of(map).max();

            assert(result1->getX()=="cba" && result1->getY()=="cab");

            std::cout<<"max-successful\n";

        }

        void testAllMatch(){
            assert((Stream<Pair<int,double>>::of(list).allMatch([](const Pair<int,double>* t){ return t->getY() < 10 && t->getX() < 10;})));

            assert(!(Stream<Pair<int,double>>::of(list).allMatch([](const Pair<int,double>* t){ return t->getY() <= 3 && t->getX() < 10;})));

            assert((Stream<Pair<string,string>>::of(map).allMatch([](const Pair<string,string>* t){ return t->getX().length()<5;})));

            std::cout<<"allMatch-successful\n";

        }

        void testAnyMatch(){
            assert((Stream<Pair<int,double>>::of(list).anyMatch([](const Pair<int,double>* t){ return t->getY() < 4 && t->getX() < 4;})));

            assert(!(Stream<Pair<int,double>>::of(list).allMatch([](const Pair<int,double>* t){ return t->getX() + t->getY() == 100;})));

            assert(!(Stream<Pair<string,string>>::of(map).allMatch([](const Pair<string,string>* t){ return t->getY().length()==1;})));


            std::cout<<"anyMatch-successful\n";

        }

        void testFindFirst(){
            auto result1=Stream<Pair<string,string>>::of(map)
                .findFirst([](const Pair<string,string>* t){ return t->getY()=="cab";});
            auto result2=Stream<Pair<string,string>>::of(map)
                .findFirst([](const Pair<string,string>* t){ return t->getY()=="bac";});


            assert(result1 != nullptr && result1->getX()=="cba");

            assert(result2== nullptr);

            auto result3=Stream<Pair<string,string>>::of(set)
                .findFirst([](const Pair<string,string>* t){ return t->getX()<t->getY();});

            assert(result3->getX()=="34");

            std::cout<<"findFirst-successful\n";
        }

        void testMixed(){
            auto result=Stream<Pair<string,string>>::of(map)
                .map<Pair<char,char>>([](const Pair<string,string>* t)
                        { return new Pair<char,char>(t->getX().at(0),t->getY().at(0));})
                .filter([](const Pair<char, char>* t){ return t->getY()==t->getX();}).distinct();

            cout << "Stream.elements = {";
            for (auto *x : result.elements)
                cout << "(" << x->getX() << ", " << x->getY() << ")" << ", ";
            cout << "}" << endl;

            assert(result.count()==2);
            //assert(result.max()->getX()=='c');
            //assert(result.min()->getY()=='a');
            //assert(result.allMatch([](const Pair<char,char>* t){return t->getY()==t->getX();}));

            //auto container=result.map<char>([](const Pair<char,char>* t)
            //        { return new char(t->getX());}).collect<std::vector<char*>>();

            //assert(container.size()==2 && *container.at(0)=='a' && *container.at(1)=='c');


        };



};



int main(){

    TestClass test;

    //test.testCreation();
    //test.testFilter();
    //test.testMap();
    //test.testDistinct();
    //test.testSorted();
    //test.testForEach();
    //test.testReduce();
    //test.testMin();
    //test.testMax();
    //test.testAllMatch();
    //test.testAnyMatch();
    //test.testFindFirst();
    test.testMixed();


    return 0;
}
