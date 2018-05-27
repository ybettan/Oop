package OOP.Solution;

import OOP.Provided.OOPExpectedException;

public class OOPExpectedExceptionImpl implements OOPExpectedException {

    private Class<? extends Exception> eClass;
    private String eMessage;


    private OOPExpectedExceptionImpl() {
        eClass = null;
        eMessage = new String();
    }

    public Class<? extends Exception> getExpectedException() {
        return eClass;
    }

    public OOPExpectedException expect(Class<? extends Exception> expected) {
        eClass = expected;
        return this;
    }

    /* according to Ophir Katz we can assume that method is called only after
     * expect() was called */
    public OOPExpectedException expectMessage(String msg) {

        /* concate the message */
        eMessage += msg;
        return this;
    }

    public boolean assertExpected(Exception e) {

        /* if we don't expect an exception the assertion fails */
        if (eClass == null)
            return false;

        /* check if e.getClass() is equal to eClass OR inherits from it */
        if (!eClass.isAssignableFrom(e.getClass()))
            return false;

        /* check the message */
        return e.getMessage().contains(eMessage);
    }

    public static OOPExpectedException none() {

        /* the default c'tor will create (null, "") */
        return new OOPExpectedExceptionImpl();
    }
}
