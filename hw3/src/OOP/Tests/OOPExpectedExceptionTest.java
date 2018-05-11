package OOP.Tests;

import OOP.Tests.Checker;
import OOP.Provided.OOPExpectedException;
import OOP.Solution.OOPExpectedExceptionImpl;

import javax.naming.NamingException;

public class OOPExpectedExceptionTest {


    public static void main(String[] args) throws Exception {

        testAll();
    }

    /* all the methods */
    private static void testAll() throws Exception {

        /* check none() */
        OOPExpectedException ee = OOPExpectedExceptionImpl.none(); 
        Checker.check(ee.getExpectedException() == null);

        /* check un ExpectedException with no message */
        ee = OOPExpectedExceptionImpl.none(); 
        ee.expect(Exception.class);
        Checker.check(ee.getExpectedException() == Exception.class);
        Checker.check(ee.assertExpected(new Exception("hello")));
        Checker.check(ee.assertExpected(new Exception("")));

        ee = OOPExpectedExceptionImpl.none(); 
        ee.expect(NamingException.class).expect(Exception.class);
        Checker.check(ee.getExpectedException() == Exception.class);
        Checker.check(ee.assertExpected(new Exception("hello")));
        Checker.check(ee.assertExpected(new Exception("")));

        /* check un ExpectedException with no message and inheritence */
        ee = OOPExpectedExceptionImpl.none(); 
        ee.expect(Exception.class);
        /* NamingException --> Exception */
        Checker.check(ee.assertExpected(new NamingException("hello")));
        Checker.check(ee.assertExpected(new NamingException("")));

        ee = OOPExpectedExceptionImpl.none(); 
        ee.expect(NamingException.class);
        Checker.check(ee.getExpectedException() == NamingException.class);
        Checker.check(ee.assertExpected(new NamingException("hello")));
        Checker.check(ee.assertExpected(new NamingException("")));
        Checker.check(!ee.assertExpected(new Exception("hello")));

        /* check un ExpectedException with message */
        ee = OOPExpectedExceptionImpl.none(); 
        ee.expect(Exception.class);
        ee.expectMessage("ell");
        Checker.check(!ee.assertExpected(new Exception("")));
        Checker.check(ee.assertExpected(new Exception("hello")));

        ee = OOPExpectedExceptionImpl.none(); 
        ee.expect(Exception.class);
        ee.expectMessage("e").expectMessage("ll");
        Checker.check(!ee.assertExpected(new Exception("")));
        Checker.check(ee.assertExpected(new Exception("hello")));

        /* check un ExpectedException with message and inheritence */
        ee = OOPExpectedExceptionImpl.none(); 
        ee.expect(Exception.class);
        ee.expectMessage("ell");
        Checker.check(!ee.assertExpected(new NamingException("")));
        Checker.check(ee.assertExpected(new NamingException("hello")));

        ee = OOPExpectedExceptionImpl.none(); 
        ee.expect(Exception.class);
        ee.expectMessage("e").expectMessage("ll");
        Checker.check(!ee.assertExpected(new NamingException("")));
        Checker.check(ee.assertExpected(new NamingException("hello")));
    }

}






