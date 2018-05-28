package OOP.Tests;

import java.util.*;
import java.lang.reflect.*;
import OOP.Tests.Checker;
import OOP.Provided.OOPResult;
import OOP.Solution.OOPResultImpl;
import OOP.Solution.OOPUnitCore;
import OOP.Solution.*;
import OOP.Provided.*;
import OOP.Provided.OOPAssertionFailure;
import OOP.Solution.OOPTestClass;
import static OOP.Provided.OOPResult.OOPTestResult.*;

import javax.naming.NamingException;

public class PrivateMethodTest {

    class Base {
        private int x;
        public String s;
        public Base(int x, String s) {
            this.x = x;
            this.s = s;
        }
        public Base(Base b1) {
            this.x = b1.x;
            this.s = new String(b1.s);
        }
    }

    class OtherBase {
        private int x;
        public String s;
        public OtherBase(int x, String s) {
            this.x = x;
            this.s = s;
        }
        public OtherBase(Base b1) {
            this.x = b1.x;
            this.s = new String(b1.s);
        }
    }

    class LargeBase {
        private double d;
        public String s;
        public Base b1;
        public LargeBase(double d, String s, Base b1) {
            this.d = d;
            this.s = s;
            this.b1 = new Base(b1);
        }
    }

    class Derived extends Base {
        private double d;
        public Derived(int x, String s, double d) {
            super(x,s);
            this.d = d;
        }
    }

    static
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    public class ExampleClass1 {

        @OOPExceptionRule
        private OOPExpectedException expected = OOPExpectedExceptionImpl.none();

        private int field = 0;

        @OOPBefore({"test1"})
        public void beforeFirstTest() {
            this.field = 123;
        }


        // Should be successful
        @OOPTest(order = 1)
        public void test1() throws OOPAssertionFailure {
            //this must run before the other test. must not throw an exception to succeed
            OOPUnitCore.assertEquals(123, this.field);
        }

        // Should fail
        @OOPTest(order = 2)
        public void test2() throws OOPAssertionFailure {
            OOPUnitCore.assertEquals(321, this.field);
        }

        // Should be successful
        @OOPTest(order = 3)
        public void testThrows() throws Exception {
            expected.expect(Exception.class)
                    .expectMessage("rror messag");
            throw new Exception("error message");
        }
    }

    static
    @OOPTestClass
    public class ExampleClass2 {

        private int field = 0;
    }

    public static void main(String[] args) throws Exception {
        
        //testBackupRecoverInst();
        //testGetExcpectedException();
        //testGetOrderedMethods();
    }

    //private static void testGetExcpectedException() throws Exception {

    //    Object testClassInst1 = null;
    //    Object testClassInst2 = null;
    //    try {
    //        testClassInst1 = ExampleClass1.class.getConstructor().newInstance();
    //        testClassInst2 = ExampleClass2.class.getConstructor().newInstance();
    //    }
    //    catch (Exception e) {}

    //    Checker.check(OOPUnitCore.getExpectedException(testClassInst2) == null);
    //    Checker.check(OOPUnitCore.getExpectedException(testClassInst1) != null);
    //}

    //private static void testGetOrderedMethods() throws Exception {

    //    Checker.check(OOPUnitCore.getOrderedSetupMethods(ExampleClass1.class).size() == 0);

    //    Vector<Method> orderedBeforeMethods =
    //        OOPUnitCore.getOrderedBeforeMethods(ExampleClass1.class, "stam");
    //    Checker.check(orderedBeforeMethods.size() == 0);

    //    orderedBeforeMethods =
    //        OOPUnitCore.getOrderedBeforeMethods(ExampleClass1.class, "test1");
    //    Checker.check(orderedBeforeMethods.size() == 1);
    //    Checker.check(orderedBeforeMethods.get(0).getName() == "beforeFirstTest");

    //    orderedBeforeMethods =
    //        OOPUnitCore.getOrderedTestMethods(ExampleClass1.class, "");
    //    Checker.check(orderedBeforeMethods.size() == 3);
    //}

}






