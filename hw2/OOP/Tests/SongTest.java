package OOP.Tests;

import OOP.Tests.Checker;
import OOP.Provided.Song;
import OOP.Provided.User;
import OOP.Solution.UserImpl;
import OOP.Solution.SongImpl;

public class SongTest {


    public static void main(String[] args)
            throws User.AlreadyFriends,
                   User.SamePerson,
                   User.IllegalRateValue,
                   User.SongAlreadyRated,
                   Exception {

        testGetters();
        testRating();
        testInheriting();
    }

    /* get{ID, Name, Length, SingerName}() */
    private static void testGetters() throws Exception {

        Song s = new SongImpl(4, "shape of you", 1, "edd shiren");
        Checker.check(s.getID() == 4);
        Checker.check(s.getName() == "shape of you");
        Checker.check(s.getLength() == 1);
        Checker.check(s.getSingerName() == "edd shiren");

        s = new SongImpl(12, "heart", 3, "cristina aguilera");
        Checker.check(s.getID() == 12);
        Checker.check(s.getName() == "heart");
        Checker.check(s.getLength() == 3);
        Checker.check(s.getSingerName() == "cristina aguilera");
    }

    /* rateSong(), getRaters(), getRatings(), getAverageRating() */
    private static void testRating() throws Exception {

        Song s = new SongImpl(4, "shape of you", 1, "edd shiren");

        /* 0 users rated user */
        Checker.check(s.getRaters().size() == 0);
        Checker.check(s.getRatings().size() == 0);
        Checker.check(s.getAverageRating() == 0);

        User u1 = new UserImpl(7, "yoni", 21);
        User u2 = new UserImpl(12, "itzik", 23);
        User u3 = new UserImpl(0, "sharon", 15);
        User u4 = new UserImpl(6, "avi", 50);
        User u5 = new UserImpl(33, "roni", 53);
        User u6 = new UserImpl(40, "puppies", 50);

        /* 1 user rated the song */
        s.rateSong(u1, 9);
        Checker.check(s.getRaters().size() == 1);
        Checker.check(s.getRatings().size() == 1);
        Checker.check(s.getRatings().containsKey(9));
        Checker.check(s.getRatings().get(9).size() == 1);
        Checker.check(s.getRatings().get(9).contains(u1));
        Checker.check(s.getAverageRating() == 9);

        /* check exceptions */
        int counter = 0;
        try {s.rateSong(u2, -2);} catch (User.IllegalRateValue e) {counter++;}
        try {s.rateSong(u2, 13);} catch (User.IllegalRateValue e) {counter++;}
        try {s.rateSong(u1, 3);} catch (User.SongAlreadyRated e) {counter++;}
        try {s.rateSong(u1, 13);} catch (User.IllegalRateValue e) {counter++;}
        Checker.check(counter == 4);

        /* multiple songs rated user */
        s.rateSong(u2, 4);
        s.rateSong(u3, 10);
        s.rateSong(u4,4);
        s.rateSong(u5, 8);
        s.rateSong(u6, 4);

        Checker.check(s.getRaters().size() == 6);
        Checker.check(s.getRaters().toArray()[0].equals(u3));
        Checker.check(s.getRaters().toArray()[1].equals(u1));
        Checker.check(s.getRaters().toArray()[2].equals(u5));
        Checker.check(s.getRaters().toArray()[3].equals(u2));
        Checker.check(s.getRaters().toArray()[4].equals(u6));
        Checker.check(s.getRaters().toArray()[5].equals(u4));
        Checker.check(s.getRatings().size() == 4);
        Checker.check(s.getRatings().containsKey(4));
        Checker.check(s.getRatings().get(4).size() == 3);
        Checker.check(s.getRatings().get(4).contains(u2));
        Checker.check(s.getRatings().get(4).contains(u4));
        Checker.check(s.getRatings().get(4).contains(u6));
        Checker.check(s.getRatings().containsKey(8));
        Checker.check(s.getRatings().get(8).size() == 1);
        Checker.check(s.getRatings().get(8).contains(u5));
        Checker.check(s.getRatings().containsKey(9));
        Checker.check(s.getRatings().get(9).size() == 1);
        Checker.check(s.getRatings().get(9).contains(u1));
        Checker.check(s.getRatings().containsKey(10));
        Checker.check(s.getRatings().get(10).size() == 1);
        Checker.check(s.getRatings().get(10).contains(u3));
        Checker.check(!s.getRatings().get(10).contains(u2));
        Checker.check(s.getAverageRating() == 6.5);
    }

    /* equals(), compareTo() */
    private static void testInheriting() throws Exception {

        Song s1 = new SongImpl(13, "shape of you", 3, "edd shiren");
        Song s2 = new SongImpl(1, "heart", 1, "cristina aguilera");
        Song s3 = new SongImpl(40, "mami", 2, "sarit hadad");
        Song s4 = new SongImpl(13, "maluma baby", 5, "maluma");
        Song s5 = new SongImpl(100, "2 crazies", 2, "omer adam");

        Checker.check(s1.equals(s1));
        Checker.check(s1.equals(s4));
        Checker.check(s4.equals(s1));
        Checker.check(!s1.equals(s2));
        Checker.check(!s2.equals(s1));

        Checker.check(s1.compareTo(s2) > 0);
        Checker.check(s2.compareTo(s1) < 0);
        Checker.check(s1.compareTo(s4) == 0);
        Checker.check(s1.compareTo(s1) == 0);
    }
}






