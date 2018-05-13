package OOP.Tests;

import OOP.Tests.Checker;
import OOP.Provided.OOPResult;
import OOP.Solution.OOPResultImpl;
import static OOP.Provided.OOPResult.OOPTestResult.*;

import javax.naming.NamingException;

public class OOPResultTest {


    public static void main(String[] args) throws Exception {

        testGetters();
        testEquals();
    }

    /* getResultType(), getMessage() */
    private static void testGetters() throws Exception {
        
        OOPResult or = new OOPResultImpl(null, null);
        Checker.check(or.getResultType() == null);
        Checker.check(or.getMessage() == null);

        or = new OOPResultImpl(SUCCESS, null);
        Checker.check(or.getResultType() == SUCCESS);
        Checker.check(or.getMessage() == null);

        or = new OOPResultImpl(null, "message");
        Checker.check(or.getResultType() == null);
        Checker.check(or.getMessage() == "message");

        or = new OOPResultImpl(SUCCESS, "message");
        Checker.check(or.getResultType() == SUCCESS);
        Checker.check(or.getMessage() == "message");

        or = new OOPResultImpl(FAILURE, "");
        Checker.check(or.getResultType() == FAILURE);
        Checker.check(or.getMessage() == "");

        or = new OOPResultImpl(ERROR, "message error");
        Checker.check(or.getResultType() == ERROR);
        Checker.check(or.getMessage() == "message error");

        or = new OOPResultImpl(EXPECTED_EXCEPTION_MISMATCH, "mismatch");
        Checker.check(or.getResultType() == EXPECTED_EXCEPTION_MISMATCH);
        Checker.check(or.getMessage() == "mismatch");
        Checker.check(!(or.getMessage() == "message error"));
    }

    private static boolean objEquals(OOPResult or1, OOPResult or2) throws Exception {

        try {
            Checker.check(or1.equals(or1));
            Checker.check(or2.equals(or2));
            Checker.check(or1.equals(or2));
            Checker.check(or2.equals(or1));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /* equals() */
    private static void testEquals() throws Exception {

        OOPResult or1 = new OOPResultImpl(null, null);
        OOPResult or2 = new OOPResultImpl(null, null);
        Checker.check(objEquals(or1, or2));

        or1 = new OOPResultImpl(null, null);
        or2 = new OOPResultImpl(null, null);
        Checker.check(objEquals(or1, or2));

        or1 = new OOPResultImpl(null, "hello");
        or2 = new OOPResultImpl(null, "hello");
        Checker.check(objEquals(or1, or2));

        or1 = new OOPResultImpl(FAILURE, null);
        or2 = new OOPResultImpl(FAILURE, null);
        Checker.check(objEquals(or1, or2));

        or1 = new OOPResultImpl(SUCCESS, "hello");
        or2 = new OOPResultImpl(SUCCESS, "hello");
        Checker.check(objEquals(or1, or2));

        or1 = new OOPResultImpl(null, null);
        or2 = new OOPResultImpl(null, "hello");
        Checker.check(!objEquals(or1, or2));

        or1 = new OOPResultImpl(null, null);
        or2 = new OOPResultImpl(SUCCESS, null);
        Checker.check(!objEquals(or1, or2));

        or1 = new OOPResultImpl(SUCCESS, "hello");
        or2 = new OOPResultImpl(SUCCESS, "other");
        Checker.check(!objEquals(or1, or2));

        or1 = new OOPResultImpl(ERROR, "hello");
        or2 = new OOPResultImpl(FAILURE, "hello");
        Checker.check(!objEquals(or1, or2));

    }

}






