package OOP.Tests;

import OOP.Tests.Checker;
import OOP.Provided.Song;
import OOP.Provided.User;
import OOP.Provided.TechnionTunes;
import OOP.Solution.UserImpl;
import OOP.Solution.SongImpl;
import OOP.Solution.TechnionTunesImpl;
import java.util.Collection;
import java.util.Set;

public class TechnionTunesTest {


    public static void main(String[] args)
            throws User.AlreadyFriends,
                   User.SamePerson,
                   User.IllegalRateValue,
                   User.SongAlreadyRated,
                   Exception {

        testAddersGetters();
        testRateStuf();
        testGetTopLikers();
        testFriendStuf();
    }

    /* addUser(), getUser(), addSong(), setSong(), sortSong() */
    private static void testAddersGetters() throws Exception {

        TechnionTunes tt = new TechnionTunesImpl();

        /* check {User,Song}DoesNotExist exception */
        int counter = 0;
        try {tt.getUser(123);} catch (TechnionTunes.UserDoesntExist e) {counter++;}
        try {tt.getSong(30);} catch (TechnionTunes.SongDoesntExist e) {counter++;}
        Checker.check(counter == 2);

        /* check sortSong on empty container */
        Checker.check(tt.sortSongs((s1,s2) -> 0).size() == 0);

        /* check {User,Song}AlreadyExist exception */
        counter = 0;
        tt.addUser(12, "yoni", 32);
        tt.addSong(23, "shape of you", 180, "edd shiren");
        try {tt.addUser(12, "yoni", 32);} catch (TechnionTunes.UserAlreadyExists e) {counter++;}
        try {tt.addSong(23, "shape of you", 180, "edd shiren");} 
            catch (TechnionTunes.SongAlreadyExists e) {counter++;}
        Checker.check(counter == 2);

        /* add {users,songs} and test getters */
        tt.addSong(29, "heart", 140, "cristina aguilera");
        tt.addUser(15, "avi", 25);
        Checker.check(tt.getSong(23).getName() == "shape of you");
        Checker.check(tt.getSong(29).getName() == "heart");
        Checker.check(tt.getUser(12).getName() == "yoni");
        Checker.check(tt.getUser(15).getName() == "avi");

        /* check sortSongs on multiple songs */
        //tt.addSong(23, "shape of you", 180, "edd shiren");
        //tt.addSong(29, "heart", 140, "cristina aguilera");
        tt.addSong(20, "listen", 100, "byonce");
        tt.addSong(24, "hello", 220, "byonce");
        tt.addSong(26, "despacito", 150, "fonsi");
        tt.addSong(21, "maluma baby", 230, "maluma");
        tt.addSong(28, "i'm bad", 80, "michel jakson");
        tt.addSong(22, "2 crazies", 190, "omer adam");

        /* sort by ID in decreasing order */
        Collection<Song> songs = tt.sortSongs((s1,s2) -> s2.getID()-s1.getID());
        Checker.check(songs.size() == 8);
        Checker.check(((Song)songs.toArray()[0]).getID() == 29);
        Checker.check(((Song)songs.toArray()[1]).getID() == 28);
        Checker.check(((Song)songs.toArray()[2]).getID() == 26);
        Checker.check(((Song)songs.toArray()[3]).getID() == 24);
        Checker.check(((Song)songs.toArray()[4]).getID() == 23);
        Checker.check(((Song)songs.toArray()[5]).getID() == 22);
        Checker.check(((Song)songs.toArray()[6]).getID() == 21);
        Checker.check(((Song)songs.toArray()[7]).getID() == 20);

        /* sort by length in increasing order */
        songs = tt.sortSongs((s1,s2) -> s1.getLength()-s2.getLength());
        Checker.check(songs.size() == 8);
        Checker.check(((Song)songs.toArray()[0]).getLength() == 80);
        Checker.check(((Song)songs.toArray()[1]).getLength() == 100);
        Checker.check(((Song)songs.toArray()[2]).getLength() == 140);
        Checker.check(((Song)songs.toArray()[3]).getLength() == 150);
        Checker.check(((Song)songs.toArray()[4]).getLength() == 180);
        Checker.check(((Song)songs.toArray()[5]).getLength() == 190);
        Checker.check(((Song)songs.toArray()[6]).getLength() == 220);
        Checker.check(((Song)songs.toArray()[7]).getLength() == 230);

    }

    /* rateSong(), getIntersection(), getHighestRatedSongs(), getMostRatedSongs(),
     * getTopLikers()  */
    private static void testRateStuf() throws Exception {
        
        TechnionTunes tt = new TechnionTunesImpl();

        tt.addUser(12, "yoni", 27);
        tt.addUser(16, "avi", 25);
        tt.addUser(11, "moti", 33);
        tt.addUser(19, "roy", 34);
        tt.addUser(18, "shir", 26);
        tt.addUser(14, "itzik", 90);

        tt.addSong(23, "shape of you", 180, "edd shiren");
        tt.addSong(20, "listen", 100, "byonce");
        tt.addSong(24, "hello", 220, "byonce");
        tt.addSong(26, "despacito", 140, "fonsi");
        tt.addSong(29, "heart", 140, "cristina aguilera");
        tt.addSong(21, "maluma baby", 230, "maluma");
        tt.addSong(28, "i'm bad", 80, "michel jakson");
        tt.addSong(22, "2 crazies", 190, "omer adam");

        /* check rateSong() exceptions */
        int counter = 0;
        try {tt.rateSong(123, 23, 9);} catch (TechnionTunes.UserDoesntExist e) {counter++;}
        try {tt.rateSong(12, 234, 9);} catch (TechnionTunes.SongDoesntExist e) {counter++;}
        try {tt.rateSong(12, 23, 11);} catch (User.IllegalRateValue e) {counter++;}
        tt.rateSong(12, 23, 2);
        try {tt.rateSong(12, 23, 8);} catch (User.SongAlreadyRated e) {counter++;}
        try {tt.rateSong(123, 230, -3);} catch (TechnionTunes.UserDoesntExist e) {counter++;}
        try {tt.rateSong(12, 230, -3);} catch (TechnionTunes.SongDoesntExist e) {counter++;}
        try {tt.rateSong(12, 23, -3);} catch (User.IllegalRateValue e) {counter++;}
        Checker.check(counter == 7);

        /* test all the rest */
        //tt.rateSong(12, 23, 2);
        tt.rateSong(12, 26, 2);
        tt.rateSong(12, 29, 2);
        tt.rateSong(12, 24, 2);

        tt.rateSong(16, 24, 6);
        tt.rateSong(16, 26, 6);
        tt.rateSong(16, 29, 6);

        tt.rateSong(19, 23, 9);
        tt.rateSong(19, 22, 9);
        tt.rateSong(19, 21, 9);

        tt.rateSong(18, 20, 8);
        tt.rateSong(18, 21, 8);

        tt.rateSong(14, 24, 4);

        /* check getIntersection */
        counter = 0;
        int[] IDs = new int[]{12, 15, 16};
        try {tt.getIntersection(new int[]{12, 15, 16});}
            catch (TechnionTunes.UserDoesntExist e) {counter++;}
        Checker.check(counter == 1);
        Set<Song> tmp = tt.getIntersection(new int[0]);
        Checker.check(tmp.size() == 0);

        tmp = tt.getIntersection(new int[]{12});
        Checker.check(tmp.size() == 4);
        for (Song s : tmp)
            Checker.check(s.getID() == 23 ||
                          s.getID() == 26 ||
                          s.getID() == 29 ||
                          s.getID() == 24);

        tmp = tt.getIntersection(new int[]{12, 14, 16, 18, 19});
        Checker.check(tmp.size() == 7);
        for (Song s : tmp)
            Checker.check(s.getID() == 23 ||
                          s.getID() == 26 ||
                          s.getID() == 29 ||
                          s.getID() == 24 ||
                          s.getID() == 22 ||
                          s.getID() == 21 ||
                          s.getID() == 20);

        tmp = tt.getIntersection(new int[]{11, 12, 14, 16, 18, 19});
        Checker.check(tmp.size() == 7);
        for (Song s : tmp)
            Checker.check(s.getID() == 23 ||
                          s.getID() == 26 ||
                          s.getID() == 29 ||
                          s.getID() == 24 ||
                          s.getID() == 22 ||
                          s.getID() == 21 ||
                          s.getID() == 20);

        /* check getHighestRatedSongs(), getMostRatedSongs() */
        Collection<Song> tmp2 = tt.getHighestRatedSongs(0);
        Checker.check(tmp2.size() == 0);

        tmp2 = tt.getHighestRatedSongs(1);
        Checker.check(tmp2.size() == 1);
        Checker.check(((Song)tmp2.toArray()[0]).getID() == 22);

        tmp2 = tt.getHighestRatedSongs(7);
        Checker.check(tmp2.size() == 7);
        Checker.check(((Song)tmp2.toArray()[0]).getID() == 22);
        Checker.check(((Song)tmp2.toArray()[1]).getID() == 21);
        Checker.check(((Song)tmp2.toArray()[2]).getID() == 20);
        Checker.check(((Song)tmp2.toArray()[3]).getID() == 23);
        Checker.check(((Song)tmp2.toArray()[4]).getID() == 24);
        Checker.check(((Song)tmp2.toArray()[5]).getID() == 26);
        Checker.check(((Song)tmp2.toArray()[6]).getID() == 29);

        tmp2 = tt.getHighestRatedSongs(10);
        Checker.check(tmp2.size() == 7);
        Checker.check(((Song)tmp2.toArray()[0]).getID() == 22);
        Checker.check(((Song)tmp2.toArray()[1]).getID() == 21);
        Checker.check(((Song)tmp2.toArray()[2]).getID() == 20);
        Checker.check(((Song)tmp2.toArray()[3]).getID() == 23);
        Checker.check(((Song)tmp2.toArray()[4]).getID() == 24);
        Checker.check(((Song)tmp2.toArray()[5]).getID() == 26);
        Checker.check(((Song)tmp2.toArray()[6]).getID() == 29);

        Collection<Song> tmp3 = tt.getMostRatedSongs(0);
        Checker.check(tmp3.size() == 0);

        tmp3 = tt.getMostRatedSongs(1);
        Checker.check(tmp3.size() == 1);
        Checker.check(((Song)tmp3.toArray()[0]).getID() == 24);

        tmp3 = tt.getMostRatedSongs(7);
        Checker.check(tmp3.size() == 7);
        Checker.check(((Song)tmp3.toArray()[0]).getID() == 24);
        Checker.check(((Song)tmp3.toArray()[1]).getID() == 29);
        Checker.check(((Song)tmp3.toArray()[2]).getID() == 26);
        Checker.check(((Song)tmp3.toArray()[3]).getID() == 23);
        Checker.check(((Song)tmp3.toArray()[4]).getID() == 21);
        Checker.check(((Song)tmp3.toArray()[5]).getID() == 20);
        Checker.check(((Song)tmp3.toArray()[6]).getID() == 22);

        tmp3 = tt.getMostRatedSongs(10);
        Checker.check(tmp3.size() == 7);
        Checker.check(((Song)tmp3.toArray()[0]).getID() == 24);
        Checker.check(((Song)tmp3.toArray()[1]).getID() == 29);
        Checker.check(((Song)tmp3.toArray()[2]).getID() == 26);
        Checker.check(((Song)tmp3.toArray()[3]).getID() == 23);
        Checker.check(((Song)tmp3.toArray()[4]).getID() == 21);
        Checker.check(((Song)tmp3.toArray()[5]).getID() == 20);
        Checker.check(((Song)tmp3.toArray()[6]).getID() == 22);
    }

    /* getTopLikers() */
    private static void testGetTopLikers() throws Exception {

        TechnionTunes tt = new TechnionTunesImpl();

        tt.addUser(12, "yoni", 27);
        tt.addUser(16, "avi", 25);
        tt.addUser(11, "moti", 33);
        tt.addUser(19, "roy", 34);
        tt.addUser(18, "shir", 34);
        tt.addUser(14, "itzik", 90);

        tt.addSong(23, "shape of you", 180, "edd shiren");
        tt.addSong(20, "listen", 100, "byonce");
        tt.addSong(24, "hello", 220, "byonce");
        tt.addSong(26, "despacito", 140, "fonsi");
        tt.addSong(29, "heart", 140, "cristina aguilera");
        tt.addSong(21, "maluma baby", 230, "maluma");
        tt.addSong(28, "i'm bad", 80, "michel jakson");
        tt.addSong(22, "2 crazies", 190, "omer adam");

        /* test all the rest */
        tt.rateSong(12, 23, 2);
        tt.rateSong(12, 26, 2);
        tt.rateSong(12, 29, 2);
        tt.rateSong(12, 24, 2);

        tt.rateSong(16, 24, 6);
        tt.rateSong(16, 26, 6);
        tt.rateSong(16, 29, 6);

        tt.rateSong(19, 23, 9);
        tt.rateSong(19, 22, 9);
        tt.rateSong(19, 21, 9);

        tt.rateSong(18, 20, 9);
        tt.rateSong(18, 21, 9);

        tt.rateSong(14, 24, 9);


        Collection<User> tmp3 = tt.getTopLikers(0);
        Checker.check(tmp3.size() == 0);

        tmp3 = tt.getTopLikers(1);
        Checker.check(tmp3.size() == 1);
        Checker.check(((User)tmp3.toArray()[0]).getID() == 14);

        tmp3 = tt.getTopLikers(6);
        Checker.check(tmp3.size() == 6);
        Checker.check(((User)tmp3.toArray()[0]).getID() == 14);
        Checker.check(((User)tmp3.toArray()[1]).getID() == 18);
        Checker.check(((User)tmp3.toArray()[2]).getID() == 19);
        Checker.check(((User)tmp3.toArray()[3]).getID() == 16);
        Checker.check(((User)tmp3.toArray()[4]).getID() == 12);
        Checker.check(((User)tmp3.toArray()[5]).getID() == 11);

        tmp3 = tt.getTopLikers(6);
        Checker.check(tmp3.size() == 6);
        Checker.check(((User)tmp3.toArray()[0]).getID() == 14);
        Checker.check(((User)tmp3.toArray()[1]).getID() == 18);
        Checker.check(((User)tmp3.toArray()[2]).getID() == 19);
        Checker.check(((User)tmp3.toArray()[3]).getID() == 16);
        Checker.check(((User)tmp3.toArray()[4]).getID() == 12);
        Checker.check(((User)tmp3.toArray()[5]).getID() == 11);

    }

    /* makeFriends(), canGetAlong() */
    private static void testFriendStuf() throws Exception {

        TechnionTunes tt = new TechnionTunesImpl();

        tt.addUser(12, "yoni", 27);
        tt.addUser(16, "avi", 25);
        tt.addUser(11, "moti", 33);
        tt.addUser(19, "roy", 34);
        tt.addUser(18, "shir", 34);
        tt.addUser(14, "itzik", 90);

        tt.addSong(23, "shape of you", 180, "edd shiren");
        tt.addSong(20, "listen", 100, "byonce");
        tt.addSong(24, "hello", 220, "byonce");
        tt.addSong(26, "despacito", 140, "fonsi");
        tt.addSong(29, "heart", 140, "cristina aguilera");
        tt.addSong(21, "maluma baby", 230, "maluma");
        tt.addSong(28, "i'm bad", 80, "michel jakson");
        tt.addSong(22, "2 crazies", 190, "omer adam");

        tt.rateSong(12, 23, 2);
        tt.rateSong(12, 26, 2);
        tt.rateSong(12, 29, 2);
        tt.rateSong(12, 24, 2);
        tt.rateSong(16, 24, 6);
        tt.rateSong(16, 26, 6);
        tt.rateSong(16, 29, 6);
        tt.rateSong(19, 23, 9);
        tt.rateSong(19, 22, 9);
        tt.rateSong(19, 21, 9);
        tt.rateSong(18, 20, 9);
        tt.rateSong(18, 21, 9);
        tt.rateSong(14, 24, 9);
        tt.rateSong(11, 20, 9);

        /* test makeFriends */
        int counter = 0;
        try {tt.makeFriends(123, 12);} catch (TechnionTunes.UserDoesntExist e) {counter++;}
        tt.makeFriends(12, 18);
        try {tt.makeFriends(18, 12);} catch (User.AlreadyFriends e) {counter++;}
        try {tt.makeFriends(11, 11);} catch (User.SamePerson e) {counter++;}
        try {tt.makeFriends(6, 6);} catch (TechnionTunes.UserDoesntExist e) {counter++;}
        Checker.check(counter == 4);

        //tt.makeFriends(12, 18);
        tt.makeFriends(12, 14);
        tt.makeFriends(18, 14);
        tt.makeFriends(18, 11);
        tt.makeFriends(18, 19);

        User u1 = tt.getUser(12);
        User u2 = tt.getUser(16);
        User u3 = tt.getUser(11);
        User u4 = tt.getUser(19);
        User u5 = tt.getUser(18);
        User u6 = tt.getUser(14);

        Checker.check(u2.getFriends().size() == 0);
        Checker.check(u4.getFriends().size() == 1);
        Checker.check(u4.getFriends().containsKey(u5));
        Checker.check(u1.getFriends().size() == 2);
        Checker.check(u1.getFriends().containsKey(u5));
        Checker.check(u1.getFriends().containsKey(u6));
        Checker.check(u5.getFriends().size() == 4);
        Checker.check(u5.getFriends().containsKey(u1));
        Checker.check(u5.getFriends().containsKey(u3));
        Checker.check(u5.getFriends().containsKey(u6));
        Checker.check(u5.getFriends().containsKey(u4));
        Checker.check(u3.getFriends().size() == 1);
        Checker.check(u3.getFriends().containsKey(u5));
        Checker.check(u6.getFriends().size() == 2);
        Checker.check(u6.getFriends().containsKey(u5));
        Checker.check(u6.getFriends().containsKey(u1));

        /* test canGetAlong */
        counter = 0;
        try {tt.canGetAlong(123, 12);} catch (TechnionTunes.UserDoesntExist e) {counter++;}
        Checker.check(counter == 1);

        Checker.check(tt.canGetAlong(12, 12));
        Checker.check(tt.canGetAlong(16, 16));
        Checker.check(tt.canGetAlong(11, 11));
        Checker.check(tt.canGetAlong(19, 19));
        Checker.check(tt.canGetAlong(18, 18));
        Checker.check(tt.canGetAlong(14, 14));

        Checker.check(tt.canGetAlong(18, 19));
        Checker.check(tt.canGetAlong(11, 19));

        Checker.check(!tt.canGetAlong(14, 19));
        Checker.check(!tt.canGetAlong(12, 19));
    }
}






