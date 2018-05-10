package OOP.Provided;

import OOP.Solution.OOPExpectedExceptionImpl;

/**
 * An interface to handle exceptions.
 */
public interface OOPExpectedException {

    /**
     * @return the expected exception type.
     */
    Class<? extends Exception> getExpectedException();

    /**
     * expect an exception with the given type to be thrown.
     *
     * @param expected - the expected exception type.
     * @return this object.
     */
    OOPExpectedException expect(Class<? extends Exception> expected);

    /**
     * expect the exception message to have a message as its substring.
     * Should be okay if the message expected is a substring of the entire exception message.
     * Can expect several messages.
     * Example: for the exception message: "aaa bbb ccc", for an OOPExpectedException e:
     * e.expectMessage("aaa").expectMessage("bb c");
     * - This should be okay.
     *
     * @param msg - the expected message.
     * @return this object.
     */
    OOPExpectedException expectMessage(String msg);

    /**
     * checks that the exception that was thrown, and passed as parameter,
     * is of a type as expected. Also checks expected message are contained in the exception message.
     * <p>
     * Should handle inheritance. For example, for expected exception A, if an exception B was thrown,
     * and B extends A, then it should be okay.
     *
     * @param e - the exception that was thrown.
     * @return whether or not the actual exception was as expected.
     */
    boolean assertExpected(Exception e);


    /**
     * @return an instance of an ExpectedException with no exception or expected messages.
     * <p>
     * This static method must be implemented in OOPExpectedExceptionImpl and return an actual object.
     * <p>
     * If this is the state of the ExpectedException object, then no exception is expected to be thrown.
     * So, if an exception is thrown, an OOPResult with ERROR should be returned
     */
    static OOPExpectedException none() {
        return OOPExpectedExceptionImpl.none();
    }
}
