package OOP.Tests;

import OOP.Tests.Checker;
import OOP.Provided.Song;
import OOP.Provided.User;
import OOP.Solution.UserImpl;
import OOP.Solution.SongImpl;

public class UserTest {


    public static void main(String[] args)
            throws User.AlreadyFriends,
                   User.SamePerson,
                   User.IllegalRateValue,
                   User.SongAlreadyRated,
                   Exception {

        testGetters();
        testRating();
        testFriends();
        testInheriting();
    }

    /* get{ID, Name, Age}() */
    private static void testGetters() throws Exception {

        User u = new UserImpl(1, "yoni", 27);
        Checker.check(u.getID() == 1);
        Checker.check(u.getName() == "yoni");
        Checker.check(u.getAge() == 27);

        u = new UserImpl(6, "itzik", 32);
        Checker.check(u.getID() == 6);
        Checker.check(u.getName() == "itzik");
        Checker.check(u.getAge() == 32);
    }

    /* rateSong(), getAverageRating(), getPlaylistLength(), getRatedSongs(),
     * getFavoriteSongs() */
    private static void testRating() throws Exception {

        User u = new UserImpl(1, "yoni", 27);

        /* 0 song rated user */
        Checker.check(u.getRatedSongs().size() == 0);
        Checker.check(u.getAverageRating() == 0);
        Checker.check(u.getPlaylistLength() == 0);
        Checker.check(u.getFavoriteSongs().size() == 0);

        Song s1 = new SongImpl(4, "shape of you", 1, "edd shiren");
        Song s2 = new SongImpl(12, "heart", 3, "cristina aguilera");
        Song s3 = new SongImpl(0, "i'm bad", 0, "michel jackson");
        Song s4 = new SongImpl(6, "2 crazies", 3, "omer adam");
        Song s5 = new SongImpl(33, "maluma baby", 2, "maluma");
        Song s6 = new SongImpl(40, "puppies", 2, "zak efron");

        /* 1 song rated user */
        u.rateSong(s1, 9);
        Checker.check(u.getRatedSongs().size() == 1);
        Checker.check(u.getAverageRating() == 9);
        Checker.check(u.getPlaylistLength() == 1);
        Checker.check(u.getFavoriteSongs().size() == 1);

        /* check exceptions */
        int counter = 0;
        try {u.rateSong(s1, 3);} catch (User.SongAlreadyRated e) {counter++;}
        try {u.rateSong(s2, -2);} catch (User.IllegalRateValue e) {counter++;}
        try {u.rateSong(s2, 13);} catch (User.IllegalRateValue e) {counter++;}
        try {u.rateSong(s1, 13);} catch (User.IllegalRateValue e) {counter++;}
        Checker.check(counter == 4);

        /* multiple songs rated user */
        u.rateSong(s2, 4).rateSong(s3, 10).rateSong(s4,4).rateSong(s5, 8)
                                                         .rateSong(s6, 4);
        Checker.check(u.getRatedSongs().size() == 6);
        Checker.check(u.getRatedSongs().toArray()[0].equals(s3));
        Checker.check(u.getRatedSongs().toArray()[1].equals(s1));
        Checker.check(u.getRatedSongs().toArray()[2].equals(s5));
        Checker.check(u.getRatedSongs().toArray()[3].equals(s6));
        Checker.check(u.getRatedSongs().toArray()[4].equals(s2));
        Checker.check(u.getRatedSongs().toArray()[5].equals(s4));
        Checker.check(u.getAverageRating() == 6.5);
        Checker.check(u.getPlaylistLength() == 11);
        Checker.check(u.getFavoriteSongs().size() == 3);
        Checker.check(u.getFavoriteSongs().toArray()[0].equals(s3));
        Checker.check(u.getFavoriteSongs().toArray()[1].equals(s1));
        Checker.check(u.getFavoriteSongs().toArray()[2].equals(s5));
    }

    /* addFriend(), getFriends(), favoriteSongInCommon() */
    private static void testFriends() throws Exception {

        User u1 = new UserImpl(13, "yoni", 27);
        User u2 = new UserImpl(1, "itzik", 16);
        User u3 = new UserImpl(40, "moty", 32);
        User u4 = new UserImpl(6, "sharon", 5);
        User u5 = new UserImpl(100, "benda", 99);

        Song s1 = new SongImpl(4, "shape of you", 1, "edd shiren");
        Song s2 = new SongImpl(12, "heart", 3, "cristina aguilera");
        Song s3 = new SongImpl(0, "i'm bad", 0, "michel jackson");
        Song s4 = new SongImpl(6, "2 crazies", 3, "omer adam");
        Song s5 = new SongImpl(33, "maluma baby", 2, "maluma");
        Song s6 = new SongImpl(40, "puppies", 2, "zak efron");

        u1.rateSong(s1, 8).rateSong(s2, 6).rateSong(s3, 9).rateSong(s4, 2)
           .rateSong(s5, 4).rateSong(s6, 10);
        u3.rateSong(s1, 7).rateSong(s2, 8).rateSong(s3, 1);
        u4.rateSong(s1, 8).rateSong(s2, 6).rateSong(s3, 9).rateSong(s4, 4)
          .rateSong(s5, 3);

        /* 0 friends */
        Checker.check(u1.getFriends().size() == 0);

        /* 1 friend */
        u1.AddFriend(u2);
        Checker.check(u1.getFriends().size() == 1);
        Checker.check(u1.getFriends().containsKey(u2));
        Checker.check(u1.getFriends().get(u2) == 0);

        /* check exceptions */
        int counter = 0;
        try {u1.AddFriend(u2);} catch (User.AlreadyFriends e) {counter++;}
        try {u1.AddFriend(u1);} catch (User.SamePerson e) {counter++;}
        Checker.check(counter == 2);

        /* multiple friends */
        u1.AddFriend(u3);
        u1.AddFriend(u4);
        Checker.check(u1.getFriends().size() == 3);
        Checker.check(u1.getFriends().containsKey(u2));
        Checker.check(u1.getFriends().containsKey(u3));
        Checker.check(u1.getFriends().containsKey(u4));
        Checker.check(!u1.getFriends().containsKey(u1));
        Checker.check(!u1.getFriends().containsKey(u5));
        Checker.check(u1.getFriends().get(u2) == 0);
        Checker.check(u1.getFriends().get(u3) == 3);
        Checker.check(u1.getFriends().get(u4) == 5);

        /* not friends */
        Checker.check(!u1.favoriteSongInCommon(u5));
        Checker.check(!u5.favoriteSongInCommon(u1));

        /* friends but not common songs */
        Checker.check(!u1.favoriteSongInCommon(u2));
        Checker.check(!u2.favoriteSongInCommon(u1));

        /* friend with songs in common but not favorite */
        Checker.check(!u1.favoriteSongInCommon(u3));
        Checker.check(!u3.favoriteSongInCommon(u1));

        /* friend with favorite songs in common */
        Checker.check(u1.favoriteSongInCommon(u4));
        Checker.check(u4.favoriteSongInCommon(u1));
    }

    /* equals(), compareTo() */
    private static void testInheriting() throws Exception {

        User u1 = new UserImpl(13, "yoni", 27);
        User u2 = new UserImpl(1, "itzik", 16);
        User u3 = new UserImpl(40, "moty", 32);
        User u4 = new UserImpl(13, "sharon", 5);
        User u5 = new UserImpl(100, "benda", 99);

        Checker.check(u1.equals(u1));
        Checker.check(u1.equals(u4));
        Checker.check(u4.equals(u1));
        Checker.check(!u1.equals(u2));
        Checker.check(!u2.equals(u1));

        Checker.check(u1.compareTo(u2) > 0);
        Checker.check(u2.compareTo(u1) < 0);
        Checker.check(u1.compareTo(u4) == 0);
        Checker.check(u1.compareTo(u1) == 0);
    }
}






