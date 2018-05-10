package OOP.Provided;


/**
 * This class represents an assertion error within a test method.
 */
public class OOPAssertionFailure extends AssertionError {

    private static final long serialVersionUID = 1L;
    private Object expected;
    private Object actual;

    /**
     * represents an assertion failure with expected and actual values.
     *
     * @param expected - The expected value.
     * @param actual   - The actual value found.
     */
    public OOPAssertionFailure(Object expected, Object actual) {
        this.expected = expected;
        this.actual = actual;
    }

    /**
     * represents an empty assertion failure.
     */
    public OOPAssertionFailure() {
    }

    @Override
    public String getMessage() {
        if (expected == null || actual == null) {   // empty assertion failure
            return "failure";
        }
        return "expected: <" + expected + "> but was: <" + actual + ">";
    }
}
