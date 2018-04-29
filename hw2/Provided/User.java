package OOP.Provided;

import java.util.Collection;
import java.util.Map;

/**
 * Created by rajaee050 on 3/19/2018.
 */
public interface User extends Comparable<User>{

    class AlreadyFriends extends Exception {}
    class SamePerson extends Exception {}
    class SongAlreadyRated extends Exception {}
    class IllegalRateValue extends Exception {}

    /**
     *
     * @return User's ID
     */
    int getID();

    /**
     *
     * @return User's Name
     */
    String getName();


    /**
     *
     * @return User's age
     */
    int getAge();

    /**
     * if the user already rated the song before then SongAlreadyRated exception should be thrown.
     * @param song that the user wants to rate
     * @param rate should be between 0 and 10 (included) , otherwise IllegalRateValue exception should be thrown.
     * @return the user himself
     */
    User rateSong(final Song song, int rate) throws IllegalRateValue,SongAlreadyRated ;

    /**
     *
     * @return the user's average rating , if the user didn't rate any song then
     * the average rating should be zero
     */
    double getAverageRating();


    /**
     *
     * @return the total length of all the songs that had been rated by the user.
     */
    int getPlaylistLength();

    /**
     *
     * @return a collection of songs that have been rated by the user .
     * the collection should be ordered as follows:
     *  1- by rating ( from higher to lower )
     *  2- by length ( from lower to higher)
     *  3- by id ( from higher to lower)
     */
    Collection<Song> getRatedSongs();

    /**
     *
     * @return all songs that had been rated by the user with 8+ ( included ) sorted by id order from
     * lower to higher.
     */
    Collection<Song> getFavoriteSongs();

    /**
     * Create a new frinedship.
     * if they already friends before AlreadyFriends exception should be thrown.
     * @param friend
     * @throws SamePerson .
     * @return
     */
    User AddFriend(User friend) throws AlreadyFriends , SamePerson;

    /**
     *
     * @param user
     * @return true if the two users are friends and have at least one song that they both
     * rated 8+ (included)
     */
    boolean favoriteSongInCommon(User user);

    /**
     *
     * @return a map of the user's friends where <key,value> = <friend ,
     *  the number of friend's rated songs >
     */
    Map<User,Integer> getFriends();


}
