package OOP.Solution;

import OOP.Provided.OOPResult;
import OOP.Provided.OOPResult.OOPTestResult;

import java.util.Map;

import static OOP.Provided.OOPResult.OOPTestResult.*;


public class OOPTestSummary {

    private Map<String, OOPResult> map;

    private int getNumRes(OOPTestResult result) {
        
        /* in case we got null as argument in c'tor - not supposed to happend */
        if (map == null)
            return 0;

        return (int) map.values()
                        .stream()
                        .filter(oopRes -> oopRes.getResultType() == result)
                        .count();
    }

    
    public OOPTestSummary(Map<String, OOPResult> map) {
        this.map = map;
    }

    public int getNumSuccesses() {
        return getNumRes(SUCCESS);
    }

    public int getNumFailures() {
        return getNumRes(FAILURE);
    }

    public int getNumErrors() {
        return getNumRes(ERROR);
    }

    public int getNumExceptionMismatches() {
        return getNumRes(EXPECTED_EXCEPTION_MISMATCH);
    }

}
