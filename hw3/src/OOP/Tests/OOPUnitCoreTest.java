package OOP.Tests;

import OOP.Tests.Checker;
import OOP.Provided.OOPResult;
import OOP.Solution.OOPResultImpl;
import OOP.Solution.OOPUnitCore;
import OOP.Provided.OOPAssertionFailure;
import OOP.Solution.OOPTestClass;
import static OOP.Provided.OOPResult.OOPTestResult.*;

import javax.naming.NamingException;

public class OOPUnitCoreTest {


    public static void main(String[] args) throws Exception {

        testAssertEquals();
        testFail();
        testRunClass();
    }

    private static void testAssertEquals() throws Exception {

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

        /* same basic/user object */
        int x1 = 3;
        String s1 = "hello";
        Double d1 = 4.5;
        Base b1 = new Base(3, "yoni");
        OtherBase ob1 = new OtherBase(3, "yoni");
        LargeBase lb1 = new LargeBase(5.5, "str", new Base(b1));
        Derived der1 = new Derived(2, "hi", 5.5);

        OOPUnitCore.assertEquals(x1, x1);
        OOPUnitCore.assertEquals(s1, s1);
        OOPUnitCore.assertEquals(d1,d1);
        OOPUnitCore.assertEquals(b1,b1);
        OOPUnitCore.assertEquals(lb1,lb1);
        OOPUnitCore.assertEquals(der1,der1);

        /* different basic/user types */
        int counter = 0;
        try {OOPUnitCore.assertEquals(x1,s1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(x1,d1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(x1,b1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(x1,ob1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(x1,lb1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(x1,der1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(s1,d1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(s1,b1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(s1,ob1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(s1,lb1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(s1,der1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(d1,b1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(d1,ob1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(d1,lb1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(d1,der1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(b1,ob1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(b1,lb1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(b1,der1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(ob1,lb1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(ob1,der1);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(lb1,der1);} catch (OOPAssertionFailure oaf) {counter++;}
        Checker.check(counter == 21);

        /* same basic/user types with different values */
        int x2 = 4; 
        String s2 = "itzik";
        Double d2 = 7.4;
        Base b2 = new Base(5, "yoni");
        Base b3 = new Base(3, "itzik");
        Base b4 = new Base(5, "itzik");
        LargeBase lb2 = new LargeBase(8.0, "str", new Base(b1));
        LargeBase lb3 = new LargeBase(5.5, "str2", new Base(b1));
        LargeBase lb4 = new LargeBase(5.5, "str", new Base(b2));
        Derived der2 = new Derived(3, "hi", 5.5);
        Derived der3 = new Derived(2, "hi2", 5.5);
        Derived der4 = new Derived(2, "hi", 8.5);

        counter = 0;
        try {OOPUnitCore.assertEquals(x1,x2);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(s1,s2);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(d1,d2);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(b1,b2);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(b1,b3);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(b1,b4);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(lb1,lb2);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(lb1,lb3);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(lb1,lb4);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(der1,der2);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(der1,der3);} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.assertEquals(der1,der4);} catch (OOPAssertionFailure oaf) {counter++;}
        Checker.check(counter == 12);

        /* same basic/user types with same values */
        int x10 = 3;
        String s10 = "hello";
        Double d10 = 4.5;
        Base b10 = new Base(3, "yoni");
        Base b11 = new Base(2, "hi");
        OtherBase ob10 = new OtherBase(3, "yoni");
        LargeBase lb10 = new LargeBase(5.5, "str", new Base(b1));
        Derived der10 = new Derived(2, "hi", 5.5);

        OOPUnitCore.assertEquals(x1, x10);
        OOPUnitCore.assertEquals(s1, s10);
        OOPUnitCore.assertEquals(d1,d10);
        OOPUnitCore.assertEquals(b1,b10);
        OOPUnitCore.assertEquals(lb1,lb10);
        OOPUnitCore.assertEquals(der1,der10);

        /* a type with its sub type */
        counter = 0;
        try {OOPUnitCore.assertEquals(der1,b11);} catch (OOPAssertionFailure oaf) {counter++;}
        Checker.check(counter == 1);

    }

    private static void testFail() throws Exception {

        int counter = 0;
        try {OOPUnitCore.fail();} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.fail();} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.fail();} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.fail();} catch (OOPAssertionFailure oaf) {counter++;}
        try {OOPUnitCore.fail();} catch (OOPAssertionFailure oaf) {counter++;}
        Checker.check(counter == 5);
    }

    private static void testRunClass() throws Exception {
        
        class NormalClass {}

        @OOPTestClass
        class TestClass {}

        /* check exception are thrown */
        int counter = 0;
        try {OOPUnitCore.runClass(null);} catch (IllegalArgumentException e) {counter++;}
        try {OOPUnitCore.runClass(null, "str");} catch (IllegalArgumentException e) {counter++;}
        try {OOPUnitCore.runClass(NormalClass.class);} catch (IllegalArgumentException e) {counter++;}
        try {OOPUnitCore.runClass(NormalClass.class, "str");} catch (IllegalArgumentException e) {counter++;}
        Checker.check(counter == 4);

        /* test logic */

        /* test order */
        
    }
}






