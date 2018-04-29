package OOP.Tests;

import OOP.Provided.Song;
import OOP.Provided.TechnionTunes;
import OOP.Provided.User;
import OOP.Solution.UserImpl;

public class UserTest {


    public static void main(String[] args) throws Exception {

        UserImpl u = new UserImpl(1, "yoni", 27);
        check(1 == 1);
    }

    private static void check(boolean cond) throws Exception {
        if (!cond) {
            throw new Exception();
        }
    }
}
