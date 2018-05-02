package OOP.Solution;

import OOP.Provided.User;
import OOP.Provided.Song;

import java.util.stream.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;

public class UserImpl implements User {

    private int userID;
    private String userName;
    private int userAge;    
    /* the songs is stored in a HashMap such that each entry has the form:
     * Song -> rate */
    private HashMap<Song,Integer> songs;
    /* all the user's friends are stored in a HashSet */
    private HashSet<User> friends;

    public UserImpl(int userID, String userName, int userAge) {
        this.userID = userID;
        /* as mentioned in FAQ userName don't need to be cloned */
        this.userName = userName;
        this.userAge = userAge;
        this.songs = new HashMap<Song,Integer>();
        this.friends = new HashSet<User>();
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
            if (songs.containsKey(song))
                throw new SongAlreadyRated();

            /* rate the sont */
            songs.put(song, rate);

            return this;
    }

    public double getAverageRating() {
        double sum = songs.values().stream().reduce(0, Integer::sum);
        int counter = songs.values().size();
        return sum/counter;
    }

    public int getPlaylistLength() {
        return songs.keySet().stream().map(s -> s.getLength())
                                      .reduce(0, Integer::sum); 
    }

    public Collection<Song> getRatedSongs() {

        /* we start from the last sort to keep a stable sort */
        return songs.keySet().stream()
                             .sorted((s1,s2) -> s1.getID() - s2.getID())     
                             .sorted((s1,s2) -> s2.getLength() - s1.getLength())     
                             .sorted((s1,s2) -> songs.get(s2) - songs.get(s1))
                             .collect(Collectors.toCollection(LinkedList::new));
    }
    
    public Collection<Song> getFavoriteSongs() {

        /* sort by the natural order (according to compareTo() ) */
        return songs.keySet().stream().filter(x -> x.getLength() >= 8)
            .sorted().collect(Collectors.toCollection(LinkedList::new));
    }

    public User AddFriend(User friend) throws AlreadyFriends , SamePerson {

        /* ckeck if they are already friends */
       if (friends.contains(friend)) 
           throw new AlreadyFriends();

       /* check it is not the same person.
        * the userID is uniq so it is equivalent of testing (userID == friendID) */
       if (friend == this)
           throw new SamePerson();

       /* add a new frendship relation */
       friends.add(friend);

       return this;
    }

    public boolean favoriteSongInCommon(User user) {
        Collection<Song> tmp = new LinkedList<Song>(getFavoriteSongs());
        tmp.retainAll(user.getFavoriteSongs());
        return tmp.size() > 0;
    }

    public Map<User,Integer> getFriends() {
        HashMap<User,Integer> res = new HashMap<User,Integer>(); 
        for (User friend : friends) {
            res.put(friend, friend.getRatedSongs().size());
        }
        return res;
    }

    /* implement Comparable<User>.compareTo() */
    public int compareTo(User other) {
        return getID() - other.getID();
    }

    /* override Object.equal */
    @Override public boolean equals(Object o) {
        if ( !(o instanceof User))
            return false;

        User other = (User)o;
        return compareTo(other) == 0;
    }
}










