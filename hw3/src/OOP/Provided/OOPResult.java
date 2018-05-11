package OOP.Provided;

/**
 * An interface that represents the result of a test.
 */
public interface OOPResult {

    /**
     * @return the result type, which is one of four possible type. See OOPTestResult.
     */
    OOPTestResult getResultType();

    /**
     * @return the message of the result in case of an error.
     */
    String getMessage();

    /**
     * Equals contract between two test results.
     */
    boolean equals(Object obj);

    /**
     * Four result types. Three error types, one for each definition in the exercise.
     */
    enum OOPTestResult {
        SUCCESS, FAILURE, ERROR, EXPECTED_EXCEPTION_MISMATCH
    }
}
