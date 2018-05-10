package OOP.Provided;

public class OOPExceptionMismatchError extends Exception {

    private Class<? extends Exception> expected;
    private Class<? extends Exception> actual;

    /**
     * @param expected - The expected exception.
     * @param actual   - The actual exception that was thrown.
     */
    public OOPExceptionMismatchError(Class<? extends Exception> expected, Class<? extends Exception> actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String getMessage() {
        return "expected exception: <" + expected.getName() + "> but <" + actual.getName() + "> was thrown";
    }
}
