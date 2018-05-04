package OOP.Solution;

import OOP.Provided.TechnionTunes;
import OOP.Provided.User;
import OOP.Provided.User.*;
import OOP.Provided.Song;

import java.util.stream.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class TechnionTunesImpl implements TechnionTunes {

    /* users: ID -> User */
    private HashMap<Integer,User> users;
    /* songs: ID -> Song */
    private HashMap<Integer,Song> songs;





    public TechnionTunesImpl() {
        users = new HashMap<Integer,User>();
        songs = new HashMap<Integer,Song>();
    }


    public void addUser(int userID , String userName , int userAge)
            throws UserAlreadyExists {
        
        /* check if user already registered */
        if (users.containsKey(userID))
            throw new UserAlreadyExists();

        /* add the user */
        users.put(userID, new UserImpl(userID, userName, userAge));
    }


	public User getUser(int id) throws UserDoesntExist {
        
        /* check if user exist */
        if (!users.containsKey(id))
            throw new UserDoesntExist();

        /* return the user */
        return users.get(id);
    }


    public void makeFriends(int id1, int id2)
            throws  UserDoesntExist , AlreadyFriends , SamePerson {

            /* check if the 2 users are registered */
            if (!(users.containsKey(id1) && users.containsKey(id2)))
                throw new UserDoesntExist();

            /* check if they are already friends */
            User u1 = users.get(id1);
            User u2 = users.get(id2);
            if (u1.getFriends().containsKey(u2))
                throw new AlreadyFriends();

            /* make friends.
             * if id1 == id2 User.AddFriend() will throw SamePerson exception */
            u1.AddFriend(u2);
            u2.AddFriend(u1);
    }


    public void addSong(int songID, String songName, int length, String SingerName)
            throws SongAlreadyExists {

        /* check if song already registered */
        if (songs.containsKey(songID))
            throw new SongAlreadyExists();

        /* add the song */
        songs.put(songID, new SongImpl(songID, songName, length, SingerName));
    }


	public Song getSong(int id) throws SongDoesntExist {

        /* check if song exist */
        if (!songs.containsKey(id))
            throw new SongDoesntExist();

        /* return the song */
        return songs.get(id);
    }


    public void rateSong(int userId, int songId, int rate)
            throws UserDoesntExist,SongDoesntExist,IllegalRateValue,SongAlreadyRated {

        /* check if user exist */
        if (!users.containsKey(userId))
            throw new UserDoesntExist();

        /* check if song exist */
        if (!songs.containsKey(songId))
            throw new SongDoesntExist();

        /* rate the song.
         * IllegalRateValue, SongAlreadyRated will be handled by sub classes */
        User u = users.get(userId);
        Song s = songs.get(songId);
        u.rateSong(s, rate);
        s.rateSong(u, rate);
    }


    public Collection<Song> sortSongs(Comparator<Song> comp) {

        return songs.values().stream().sorted(comp)
                    .collect(Collectors.toCollection(LinkedList::new));
    }


    public Set<Song> getIntersection(int IDs[]) throws UserDoesntExist {

        Set<Song> res = new HashSet<Song>();
        for (int id : IDs) {

            /* get all user's rated song.
             * throw UserDoesntExist if the user isn't registered */
            Collection<Song> userRatedSongs = getUser(id).getRatedSongs(); 

            /* add all user =rated= songs to the result */
            for (Song s : userRatedSongs) {
                res.add(s);
            }
        }
        return res;
    }


    public Collection<Song> getHighestRatedSongs(int num) {
        
        /* get all rated songs IDs */
        int[] userIDs = users.keySet().stream().mapToInt(Number::intValue).toArray();
        Set<Song> allRatedSongs = null;
        try {allRatedSongs = getIntersection(userIDs);}
        catch (UserDoesntExist e) {/* we should not arrive here */}

        /* sort and sub result */
        ArrayList<Song> res = allRatedSongs
                .stream()
                .sorted() /* by id in increasing order */
                .sorted((s1,s2) -> s2.getLength() - s1.getLength())
                .sorted((s1,s2) -> {
                            double tmp = s2.getAverageRating() - s1.getAverageRating();
                            return tmp > 0 ? 1 : (tmp < 0 ? -1 : 0);
                            })
                .collect(Collectors.toCollection(ArrayList::new));
        if (num < res.size())
            return res.subList(0, num);
        else
            return res;
    }


    public Collection<Song> getMostRatedSongs(int num) {

        /* get all rated songs IDs */
        int[] userIDs = users.keySet().stream().mapToInt(Number::intValue).toArray();
        Set<Song> allRatedSongs = null;
        try {
                allRatedSongs = getIntersection(userIDs);
            } catch (UserDoesntExist e) {
                /* we should not arrive here */
            }

        /* sort and sub result */
        ArrayList<Song> res = allRatedSongs
                .stream()
                .sorted((s1,s2) -> s2.getID() - s1.getID())
                .sorted((s1,s2) -> s1.getLength() - s2.getLength())
                .sorted((s1,s2) -> s2.getRaters().size() - s1.getRaters().size())
                .collect(Collectors.toCollection(ArrayList::new));
        if (num > res.size())
            return res;
        else
            return res.subList(0, num);
    }
    

    public Collection<User> getTopLikers(int num) {

        /* sort and sub result */
        return users.values()
                .stream()
                .sorted() /* by ID in increasing order */
                .sorted((u1,u2) -> u2.getAge() - u1.getAge())
                .sorted((u1,u2) -> {
                            double tmp = u2.getAverageRating() - u1.getAverageRating();
                            return tmp > 0 ? 1 : (tmp < 0 ? -1 : 0);
                            })
                .collect(Collectors.toCollection(ArrayList::new))
                .subList(0, num);
    }

    /* work as expected from canGetAlong() but with a visited set */
    private boolean canGetAlongAux(int userId1, int userId2, Set<Integer> visited)
            throws UserDoesntExist {

        /* mark the current node as visited */
        visited.add(userId1);

        /* make sure both users are registered */
        if (!(users.containsKey(userId1) && users.containsKey(userId2)))
            throw new UserDoesntExist();

        /* a user get along with himself */
        if (userId1 == userId2)
            return true;
        
        /* to friends with at least 1 favorite song in common */
        User u1 = users.get(userId1);
        User u2 = users.get(userId2);
        if (u1.getFriends().containsKey(u2))
            return u1.favoriteSongInCommon(u2);

        /* try to find a path between 2 not-freinds users */
        for (User friend : u1.getFriends().keySet()) {

            /* visit only new nodes */
            if (visited.contains(friend.getID()))
                continue;

            /* marke new node as visited and call recursively */
            if (canGetAlong(userId1, friend.getID()) &&
                canGetAlongAux(friend.getID(), userId2, visited)) {
                return true;
            }
        }
        return false;
    }

    public boolean canGetAlong(int userId1, int userId2) throws UserDoesntExist {

        /* we will make sure no infinity recusive is done by managing a
         * visited set */
        Set<Integer> visited = new HashSet<Integer>();
        return canGetAlongAux(userId1, userId2, visited);
    }

    /* implementing an Iterator to be returned in iterator() method */
    class IteratorImpl implements Iterator<Song> {

        private int nextIndex;
        private int size;


        public IteratorImpl(int size) {
            nextIndex = 0;
            this.size = size;
        }

        public boolean hasNext() {
            return nextIndex < size;
        }

        public Song next() {
            return (Song) songs.keySet().toArray()[nextIndex++];
        }

    }


    public Iterator<Song> iterator() {

        return new IteratorImpl(songs.values().size());
    }
}




