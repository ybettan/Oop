package OOP.Tests;

import OOP.Provided.Song;
import OOP.Provided.TechnionTunes;
import OOP.Provided.User;
import OOP.Solution.SongImpl;
import OOP.Solution.TechnionTunesImpl;
import org.junit.Assert;
import org.junit.Test;

public class Example {

    @Test
    public void exampleTest() throws TechnionTunes.UserAlreadyExists, TechnionTunes.UserDoesntExist, User.AlreadyFriends, User.SamePerson, TechnionTunes.SongAlreadyExists, User.IllegalRateValue, TechnionTunes.SongDoesntExist, User.SongAlreadyRated {
        TechnionTunes tt = new TechnionTunesImpl();
        tt.addUser(125,"Rajaie",5);
        tt.addUser(200,"Natan",5);
        Assert.assertEquals("Rajaie",tt.getUser(125).getName());
        Assert.assertEquals("Natan",tt.getUser(200).getName());

        tt.makeFriends(125,200);


        tt.addSong(321,"Despacito",300,"Daddy Yankee");
        Assert.assertEquals("Despacito",tt.getSong(321).getName());
        tt.rateSong(125,321,8);
        tt.rateSong(200,321,10);

        Assert.assertTrue(tt.getUser(125).getRatedSongs().contains(new SongImpl(321,"haha",300,"hehe")));
        for(Song s : tt){
            Assert.assertTrue(tt.getUser(125).getRatedSongs().contains(s));
        }

        Assert.assertTrue(tt.canGetAlong(125,200));
    }

}
