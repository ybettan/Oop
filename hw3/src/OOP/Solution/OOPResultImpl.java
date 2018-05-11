package OOP.Solution;

import OOP.Provided.OOPResult;

public class OOPResultImpl implements OOPResult {

    private OOPTestResult res;
    private String msg;


    public OOPResultImpl(OOPTestResult res, String msg) {
        this.res = res;
        this.msg = msg;
    }

    public OOPTestResult getResultType() {
        return res;
    }

    public String getMessage() {
        return msg;
    }

    public boolean equals(Object obj) {
        return getResultType() == ((OOPResult)obj).getResultType() &&
               getMessage() == ((OOPResult)obj).getMessage();
    }
} 
