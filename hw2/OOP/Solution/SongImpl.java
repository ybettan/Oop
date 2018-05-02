package OOP.Solution;

import OOP.Provided.Song;
import OOP.Provided.User;

import java.util.stream.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;

public class SongImpl implements Song {

    private int songID;
    private String songName;
    private int length;
    private String singerName;
    /* the user's rates are stored in a HashMap such that each entry has the form:
     * User -> rate */
    private HashMap<User,Integer> usersRate;

    public SongImpl(int songID, String songName, int length, String singerName) {
        this.songID = songID;
        /* as mentioned in FAQ songName don't need to be cloned */
        this.songName = songName;
        this.length = length;
        /* as mentioned in FAQ singerName don't need to be cloned */
        this.singerName = singerName;
        this.usersRate = new HashMap<User,Integer>();
    }

    public int getID() {
        return songID;
    }

    public String getName() {
        return songName;
    }

    public int getLength() {
        return length;
    }

    public String getSingerName() {
        return singerName;
    }

    public void rateSong(final User user, int rate)
            throws User.IllegalRateValue,User.SongAlreadyRated {

            /* check rate input */
            if (rate < 0 || rate > 10)
                throw new User.IllegalRateValue();
                
            /* check if this user already rated the song */
            if (usersRate.containsKey(user))
                throw new User.SongAlreadyRated();

            /* rate the sont */
            usersRate.put(user, rate);
    }

    public Collection<User> getRaters() {

        /* we start from the last sort to keep a stable sort */
        return usersRate.keySet().stream()
                             .sorted()     
                             .sorted((u1,u2) -> u2.getAge() - u1.getAge())     
                             .sorted((u1,u2) -> usersRate.get(u2) - usersRate.get(u1))
                             .collect(Collectors.toCollection(LinkedList::new));
    }

    public Map<Integer,Set<User>> getRatings() {

        HashMap<Integer,Set<User>> res = new HashMap<Integer,Set<User>>();

        /* get all rates with no duplications */
        Collection<Integer> rates = usersRate.values(); 
        HashSet<Integer> setRates = new HashSet<Integer>(rates);
        
        /* for each rate find all the relevant user and insert it to a Map */
        for (Integer r : setRates) {

            /* create a Set<User> that contains all the users that rated this
             * song with rate == r */
            HashSet<User> rateUsers = 
                usersRate.entrySet()
               .stream()
               .filter(e -> e.getValue() == r)
               .map(e -> e.getKey())
               .collect(Collectors.toCollection(HashSet::new));
            
            /* insert the rate and the set to the map */
            res.put(r, rateUsers);            
        }
        return res;
    }

    public double getAverageRating() {
        int numRaters = usersRate.size();
        double sumRates = usersRate.values().stream().reduce(0, Integer::sum);
        return sumRates/numRaters;
    }

    /* implement Comparable<Song>.compareTo() */
    public int compareTo(Song other) {
        return getID() - other.getID();
    }

    /* override Object.equal */
    @Override public boolean equals(Object o) {
        if ( !(o instanceof Song))
            return false;

        Song other = (Song)o;
        return compareTo(other) == 0;
    }
}










