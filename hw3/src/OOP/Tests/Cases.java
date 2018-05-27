package OOP.Tests;


import OOP.Solution.*;
import OOP.Provided.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/*
    Cases we should check:
        1. setup V
        2. setup inherited V
        3. setup override V
        4. before V
        5. before override V
        6. after V
        7. after override V
        8. exception in before V
        9. exception in after V
        10. expected exception V (ExpectedException2,ExpectedException3)
        11. none expected exception V (ExpectedException1)
        12. before fails, and a different test runs after V
        13. after fails, and a different test runs after V
        14. copy C'tor backup V
        15. no clone and no copy C'tor (should keep the assigned value)
        16. a lot of before methods (can be simply implemented with simple fields)
        17. a lot of after methods (can be simply implemented with simple fields)
        18. a lot of setup methods (can be simply implemented with simple fields)
        19. 16, 17, 18 together (should be easy by inheritance)
        20. a lot of expected exceptions (is this allowed?)
        21. Tests Methods Override V (Base,Derived1,Derived2)
 */
public class Cases {
    static String before = "before";
    static String afterSetUp = "after set up";
    static String afterSetUpOverride = "after set up override";
    static String afterBeforeOverride = "after before override";
    static String afterBefore = "after before";
    static String afterAfter = "after after";
    static String afterAfterOverride = "after after override";
    final static String tag = "Tag";
    OOPTestSummary result;

    /************************************************************************/
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testSetUpClass {
        public String s;

        public testSetUpClass(){
            s = before;
        }

        @OOPSetup
        public void setup() {
            this.s = afterSetUp;
        }

        @OOPTest(order = 1)
        public void test1() {
            OOPUnitCore.assertEquals(this.s, afterSetUp);
        }
    }

    @Test
    public void testSetUp() {
        result = OOPUnitCore.runClass(testSetUpClass.class);
        assertNotNull(result);
        assertEquals(1, result.getNumSuccesses());
        assertEquals(0, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testBeforeClass extends testSetUpClass {


        @OOPBefore({"test2"})
        public void beforeTest2(){
            s = afterBefore;
        }

        @OOPTest(order = 2)
        public void test2(){
            OOPUnitCore.assertEquals(this.s, afterBefore);
        }
    }

    @Test
    public void testBefore() {
        result = OOPUnitCore.runClass(testBeforeClass.class);
        assertNotNull(result);
        assertEquals(2, result.getNumSuccesses());
        assertEquals(0, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testAfterClass extends testBeforeClass {

        @OOPAfter({"test2"})
        public void afterTest2() {
            this.s = afterAfter;
        }

        @OOPTest(order=3)
        public void test3() {
            OOPUnitCore.assertEquals(this.s, afterAfter);
        }
    }

    @Test
    public void testAfter() {
        result = OOPUnitCore.runClass(testAfterClass.class);
        assertNotNull(result);
        assertEquals(3, result.getNumSuccesses());
        assertEquals(0, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testFailClass extends testAfterClass {

        @OOPTest(order=4)
        public void test4(){
            OOPUnitCore.fail();
        }
    }

    @Test
    public void testAssertionFailure() {
        result = OOPUnitCore.runClass(testFailClass.class);
        assertNotNull(result);
        assertEquals(3, result.getNumSuccesses());
        assertEquals(1, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }

    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testTagTestsClass extends testFailClass {

        @OOPTest(tag = tag, order = 5)
        public void willRun1(){
            OOPUnitCore.fail();
        }

        @OOPTest(tag = tag, order = 6)
        public void willRun2(){
            OOPUnitCore.fail();
        }

        @OOPTest(order = 7)
        public void wontRun(){
            OOPUnitCore.fail();
        }
    }

    @Test
    public void testTag() {
        result = OOPUnitCore.runClass(testTagTestsClass.class, tag);
        assertNotNull(result);
        assertEquals(0, result.getNumSuccesses());
        assertEquals(2, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());

        result = OOPUnitCore.runClass(testTagTestsClass.class);
        assertNotNull(result);
        assertEquals(3, result.getNumSuccesses());
        assertEquals(4, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testFailBeforeClass extends testTagTestsClass {
        public String backMeUp;

        @OOPSetup
        public void setupThrow() {
            backMeUp = afterSetUp;
        }

        @OOPBefore({"beforeThrow"})
        public void willThrow() throws Exception {
            backMeUp = afterSetUp;
            s = before;
            throw new Exception();
        }

        @OOPTest(order = 8)
        public void beforeThrow(){
            OOPUnitCore.fail();
        }

        @OOPTest(order = 9)
        public void afterThrow() {
            OOPUnitCore.assertEquals(s, before);
            OOPUnitCore.assertEquals(backMeUp, afterSetUp);
        }
    }

    @Test
    public void testFailBefore() {
        result = OOPUnitCore.runClass(testFailBeforeClass.class);
        assertNotNull(result);
        assertEquals(4, result.getNumSuccesses());
        assertEquals(4, result.getNumFailures());
        assertEquals(1, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testFailAfterClass extends testTagTestsClass {
        public String backMeUp;

        @OOPSetup
        public void setupThrow() {
            backMeUp = afterSetUp;
        }

        @OOPAfter({"throwAfter1"})
        public void willThrow() throws Exception {
            backMeUp = afterAfter;
            throw new Exception();
        }

        // fail because after will fail.
        @OOPTest(order = 8)
        public void throwAfter1() {
            backMeUp = before;
        }

        // Success
        @OOPTest(order = 9)
        public void worksAfter1(){
            OOPUnitCore.assertEquals(backMeUp, before);
        }
    }

    @Test
    public void testFailAfter() {
        result = OOPUnitCore.runClass(testFailAfterClass.class);
        assertNotNull(result);
        assertEquals(4, result.getNumSuccesses());
        assertEquals(4, result.getNumFailures());
        assertEquals(1, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }

    /************************************************************************/
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testSetUpOverrideClass extends testSetUpClass {

        @OOPSetup
        @Override
        public void setup() {
            this.s = afterSetUpOverride;
        }

        @OOPTest(order = 2)
        public void test1_2() {
            OOPUnitCore.assertEquals(this.s, afterSetUpOverride);
        }
    }

    @Test
    public void testSetUpOverride() {
        result = OOPUnitCore.runClass(testSetUpOverrideClass.class);
        assertNotNull(result);
        // TODO: our we sure that the original setup should not run before the original test? (test1)
        assertEquals(1, result.getNumSuccesses());
        assertEquals(1, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testAfterOverrideClass extends testAfterClass {

        @OOPAfter({"test2"})
        @Override
        public void afterTest2() {
            this.s = afterAfterOverride;
        }

        @OOPTest(order=4)
        @Override
        public void test3() {
            OOPUnitCore.assertEquals(this.s, afterAfterOverride);
        }
    }

    @Test
    public void testAfterOverride() {
        result = OOPUnitCore.runClass(testAfterOverrideClass.class);
        assertNotNull(result);
        assertEquals(3, result.getNumSuccesses());
        assertEquals(0, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class testBeforeOverrideClass extends testBeforeClass {


        @OOPBefore({"test2"})
        @Override
        public void beforeTest2(){
            s = afterBeforeOverride;
        }

        @OOPTest(order = 3)
        @Override
        public void test2(){
            OOPUnitCore.assertEquals(this.s, afterBeforeOverride);
        }
    }

    @Test
    public void testBeforeOverride() {
        result = OOPUnitCore.runClass(testBeforeOverrideClass.class);
        assertNotNull(result);
        assertEquals(2, result.getNumSuccesses());
        assertEquals(0, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/
    static final int CTOR_INT = 0;
    static final int COPY_CTOR_INT = 1;
    static final int BEFORE_INT = 2;
    static final int AFTER_INT = 3;

    public static class hasCopyCtorInt {
        public int publicInt;

        public hasCopyCtorInt() {
            publicInt = CTOR_INT;
        }

        public hasCopyCtorInt(hasCopyCtorInt other) {
            publicInt = COPY_CTOR_INT;
        }
    }
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    public static class testCopyCtorClass {
        public hasCopyCtorInt hasCopyCtor_int;

        public testCopyCtorClass() {
            hasCopyCtor_int = new hasCopyCtorInt();
        }

        @OOPBefore({"test2"})
        public void beforeTest2() throws Exception {
            hasCopyCtor_int.publicInt = BEFORE_INT;
            throw new Exception();
        }

        @OOPAfter({"test4"})
        public void afterTest4() throws Exception {
            hasCopyCtor_int.publicInt = AFTER_INT;
            throw new Exception();
        }

        // Success
        @OOPTest(order = 1)
        public void test1() {
            OOPUnitCore.assertEquals(hasCopyCtor_int.publicInt, CTOR_INT);
        }

        // Error
        @OOPTest(order = 2)
        public void test2(){
            OOPUnitCore.fail();
        }

        // Success
        @OOPTest(order = 3)
        public void test3() {
            OOPUnitCore.assertEquals(hasCopyCtor_int.publicInt, COPY_CTOR_INT);
        }

        // Error
        @OOPTest(order = 4)
        public void test4() { }

        // Success
        @OOPTest(order = 5)
        public void test5() {
            OOPUnitCore.assertEquals(hasCopyCtor_int.publicInt, COPY_CTOR_INT);
        }
    }

    @Test
    public void testCopyCtor() {
        result = OOPUnitCore.runClass(testCopyCtorClass.class);
        assertNotNull(result);
        assertEquals(3, result.getNumSuccesses());
        assertEquals(0, result.getNumFailures());
        assertEquals(2, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }
    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class Base {
        private String s = "";

        //Should Never Called
        public void Empty() {
            s = "Empty";
        }

        //Failed
        @OOPTest(order = 1)
        public void test1() {
            OOPUnitCore.assertEquals(s,"Hi");
        }

        //Success
        @OOPTest(order = 2)
        public void test2() {
            OOPUnitCore.assertEquals(s,"");
        }

        //Success
        @OOPTest(order = 3)
        public void test3() {
            OOPUnitCore.assertEquals(s,"");
        }
    }

    @Test
    public void testBase() {
        result = OOPUnitCore.runClass(Base.class);
        assertNotNull(result);
        assertEquals(2, result.getNumSuccesses());
        assertEquals(1, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class Derived1 extends Base {
        private String s = "";

        //Success
        @OOPTest(order = 4)
        @Override
        public void Empty() {
            s = "Empty";
        }

        //Success
        @OOPTest(order = 5)
        @Override
        public void test1() {
            OOPUnitCore.assertEquals(s,"Empty");
        }

        //Failed
        @OOPTest(order = 6)
        @Override
        public void test2() {
            OOPUnitCore.assertEquals(s,"");
        }
    }

    @Test
    public void testDerived1() {
        result = OOPUnitCore.runClass(Derived1.class);
        assertNotNull(result);
        assertEquals(3, result.getNumSuccesses());
        assertEquals(1, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class Derived2 extends Derived1 {
        private String s = "";

        //Failure
        @OOPTest(order = 7)
        @Override
        public void test1() {
            OOPUnitCore.fail();
        }

        //Failed
        @OOPTest(order = 8)
        @Override
        public void test3() {
            OOPUnitCore.assertEquals(s,"Empty");
        }
    }

    @Test
    public void testDerived2() {
        result = OOPUnitCore.runClass(Derived2.class);
        assertNotNull(result);
        assertEquals(1, result.getNumSuccesses());
        assertEquals(3, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }

    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.UNORDERED)
    static public class ExpectedException1 {
        @OOPExceptionRule
        private OOPExpectedException expected = OOPExpectedExceptionImpl.none();

        private String field = "";

        //Success
        @OOPTest
        public void test1() {
            OOPUnitCore.assertEquals(field,"");
        }

        //Failed
        @OOPTest
        public void test2() {
            OOPUnitCore.assertEquals(field,"Bye");
        }

        //Error
        @OOPTest
        public void test3() throws Exception {
            throw new Exception();
        }
    }

    @Test
    public void testExpectedException1() {
        result = OOPUnitCore.runClass(ExpectedException1.class);
        assertNotNull(result);
        assertEquals(1, result.getNumSuccesses());
        assertEquals(1, result.getNumFailures());
        assertEquals(1, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }

    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.UNORDERED)
    static public class ExpectedException2 {
        @OOPExceptionRule
        private OOPExpectedException expected = OOPExpectedExceptionImpl.none();

        private String field = "";

        //Error : Expected Exception but no Exception has thrown
        @OOPTest
        public void test1() {
            expected = OOPExpectedException.none();
            expected.expect(Exception.class)
                    .expectMessage("Error");
            OOPUnitCore.assertEquals(field,"");
        }

        //Success
        @OOPTest
        public void test2() throws Exception {
            expected = OOPExpectedException.none();
            expected.expect(Exception.class)
                    .expectMessage("Error");
            throw new Exception("Error");
        }

        //Success
        @OOPTest
        public void test3() throws Exception {
            expected = OOPExpectedException.none();
            expected.expect(Exception.class)
                    .expectMessage("error");
            throw new NoSuchMethodException("error");
        }

        //EXPECTED_EXCEPTION_MISMATCH
        @OOPTest
        public void test4() throws Exception {
            expected = OOPExpectedExceptionImpl.none();
            expected.expect(NoSuchMethodException.class)
                    .expectMessage("error");
            throw new Exception("error");
        }

        //EXPECTED_EXCEPTION_MISMATCH
        @OOPTest
        public void test5() throws Exception {
            expected = OOPExpectedExceptionImpl.none();
            expected.expect(Exception.class)
                    .expectMessage("Bye");
            throw new Exception("error");
        }

        //FAILURE
        @OOPTest
        public void test6() throws Exception {
            expected = OOPExpectedExceptionImpl.none();
            expected.expect(Exception.class)
                    .expectMessage("Bye");
            OOPUnitCore.assertEquals(field,"Hi");
        }

        //FAILURE
        @OOPTest
        public void test7() throws Exception {
            expected = OOPExpectedExceptionImpl.none();
            OOPUnitCore.assertEquals(field,"Hi");
        }

        //ERROR
        @OOPTest
        public void test8() throws Exception {
            expected = OOPExpectedExceptionImpl.none();
            throw new Exception("Hi");
        }
    }

    @Test
    public void testExpectedException2() {
        result = OOPUnitCore.runClass(ExpectedException2.class);
        assertNotNull(result);
        assertEquals(2, result.getNumSuccesses());
        assertEquals(2, result.getNumFailures());
        assertEquals(2, result.getNumErrors());
        assertEquals(2, result.getNumExceptionMismatches());
    }

    /************************************************************************/

    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    static public class ExpectedException3 {
        @OOPExceptionRule
        private OOPExpectedException expected = OOPExpectedExceptionImpl.none();

        private String field = "";

        //Error : Expected Exception but no Exception has thrown
        @OOPTest(order = 1)
        public void test1() {
            expected.expect(Exception.class)
                    .expectMessage("Error");
            OOPUnitCore.assertEquals(field,"");
        }

        //Success
        @OOPTest(order = 2)
        public void test2() throws Exception {
            expected = OOPExpectedExceptionImpl.none();
            expected.expect(Exception.class)
                    .expectMessage("Error");
            throw new Exception("Error");
        }

        //Success
        @OOPTest(order = 3)
        public void test3() throws Exception {
            expected = OOPExpectedExceptionImpl.none();
            expected.expect(Exception.class)
                    .expectMessage("Error").expectMessage("Bye");
            throw new Exception("Bye Error");
        }

        //EXPECTED_EXCEPTION_MISMATCH : Messages doesn't correct
        @OOPTest(order = 4)
        public void test4() throws Exception {
            expected = OOPExpectedExceptionImpl.none();
            expected.expect(Exception.class)
                    .expectMessage("Error").expectMessage("Bye");
            throw new Exception("ByError");
        }

        //Error : Wasn't Expecting Exception
        @OOPTest(order = 5)
        public void test5() throws Throwable {
            expected = OOPExpectedExceptionImpl.none();
            throw new Exception("Bye");
        }

    }

    @Test
    public void testExpectedException3() {
        result = OOPUnitCore.runClass(ExpectedException3.class);
        assertNotNull(result);
        assertEquals(2, result.getNumSuccesses());
        assertEquals(0, result.getNumFailures());
        assertEquals(2, result.getNumErrors());
        assertEquals(1, result.getNumExceptionMismatches());
    }

}
