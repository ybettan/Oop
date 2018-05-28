package OOP.Tests;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPExpectedException;
import OOP.Solution.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class SetupOrderTest {

    public static int global = 0;

    @Test
    public void testForExample() {

        OOPTestSummary result = OOPUnitCore.runClass(ExampleClass2.class);
        assertNotNull(result);
        assertEquals(0, result.getNumSuccesses());
        assertEquals(0, result.getNumFailures());
        assertEquals(0, result.getNumErrors());
        assertEquals(0, result.getNumExceptionMismatches());
    }

    static
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    public class ExampleClass0 {

        @OOPExceptionRule
        private OOPExpectedException expected = OOPExpectedExceptionImpl.none();

        @OOPSetup
        private void setup0() throws Exception {
            Checker.check(global == 0);
            global = 1;
        }
    }

    static
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    public class ExampleClass1 extends ExampleClass0 {

        @OOPSetup
        protected void setup1() throws Exception {
            Checker.check(global == 1);
            global = 2;
        }
    }

    static
    @OOPTestClass(OOPTestClass.OOPTestClassType.ORDERED)
    public class ExampleClass2 extends ExampleClass1 {

        @OOPSetup
        public void setup2() throws Exception {
            Checker.check(global == 2);
            global = 1;
        }
    }
}
