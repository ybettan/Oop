package OOP.Solution;

import OOP.Provided.User;
import OOP.Provided.Song;

import java.util.stream;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector; //FIXME: remove

public class UserImpl implements User {

    private int userID;
    private String userName;
    private int userAge;    
    /* the songs is stored in a HashMap such that each entry has the form:
     * songID -> rate */
    private HashMap<Integer,Integer> songs;

    public UserImpl(int userID, String userName, int userAge) {
        this.userID = userID;
        /* as mentioned in FAQ userName don't need to be cloned */
        this.userName = userName;
        this.userAge = userAge;
        this.songs = new HashMap<Integer,Integer>();
    }

    public int getID() {
        return userID;
    }

    public String getName() {
        return userName;
    }

    public int getAge() {
        return userAge;
    }

    public User rateSong(final Song song, int rate)
            throws IllegalRateValue, SongAlreadyRated {

            /* check rate input */
            if (rate < 0 || rate > 10)
                throw new IllegalRateValue();
                
            /* check if song was already rated */
            if (songs.containsKey(song.getID()))
                throw new SongAlreadyRated();

            /* rate the sont */
            songs.put(song.getID(), rate);

            return this;
    }

    public double getAverageRating() {

        double sum = songs.values().stream().sum();
        int counter = songs.values().size();
        return sum/counter;
    }

    public int getPlaylistLength() {return 1;}

    public Collection<Song> getRatedSongs() {return new Vector<Song>();}
    
    public Collection<Song> getFavoriteSongs() {return new Vector<Song>();}

    public User AddFriend(User friend) throws AlreadyFriends , SamePerson {return new UserImpl(1, "yoni", 27);}

    public boolean favoriteSongInCommon(User user) {return true;}

    public Map<User,Integer> getFriends() {return new HashMap<User,Integer>();}

    /* implement Comparable<User>.compareTo() */
    public int compareTo(User other) {return 1;}
}










