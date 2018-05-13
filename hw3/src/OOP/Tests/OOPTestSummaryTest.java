package OOP.Tests;

import OOP.Tests.Checker;
import OOP.Provided.OOPResult;
import OOP.Solution.OOPResultImpl;
import OOP.Solution.OOPTestSummary;
import static OOP.Provided.OOPResult.OOPTestResult.*;

import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;

public class OOPTestSummaryTest {


    public static void main(String[] args) throws Exception {

        testAll();
    }

    /* getNumSuccesses(), getNumFailures(), getNumErrors(),
     * getNumExceptionMismatches() */
    private static void testAll() throws Exception {
        
        OOPTestSummary ots = new OOPTestSummary(null);
        Checker.check(ots.getNumSuccesses() == 0);
        Checker.check(ots.getNumFailures() == 0);
        Checker.check(ots.getNumErrors() == 0);
        Checker.check(ots.getNumExceptionMismatches() == 0);

        ots = new OOPTestSummary(new HashMap<String, OOPResult>());
        Checker.check(ots.getNumSuccesses() == 0);
        Checker.check(ots.getNumFailures() == 0);
        Checker.check(ots.getNumErrors() == 0);
        Checker.check(ots.getNumExceptionMismatches() == 0);

        Map<String, OOPResult> hMap = new HashMap<String, OOPResult>();
        hMap.put("m1", new OOPResultImpl(SUCCESS, "msg"));
        hMap.put("m2", new OOPResultImpl(SUCCESS, "msg"));
        hMap.put("m3", new OOPResultImpl(SUCCESS, "msg"));
        hMap.put("m4", new OOPResultImpl(SUCCESS, "msg"));
        hMap.put("m5", new OOPResultImpl(FAILURE, "msg"));
        hMap.put("m6", new OOPResultImpl(FAILURE, "msg"));
        hMap.put("m7", new OOPResultImpl(FAILURE, "msg"));
        hMap.put("m8", new OOPResultImpl(ERROR, "msg"));
        hMap.put("m9", new OOPResultImpl(ERROR, "msg"));
        hMap.put("m10", new OOPResultImpl(EXPECTED_EXCEPTION_MISMATCH, "msg"));
        ots = new OOPTestSummary(hMap);
        Checker.check(ots.getNumSuccesses() == 4);
        Checker.check(ots.getNumFailures() == 3);
        Checker.check(ots.getNumErrors() == 2);
        Checker.check(ots.getNumExceptionMismatches() == 1);

    }

}






