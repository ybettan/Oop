package OOP.Tests;

public class Checker {

    static void check(boolean cond) throws Exception {
        if (!cond) {
            throw new Exception();
        }
    }
}
