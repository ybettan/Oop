package OOP.Provided;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import OOP.Provided.User.*;

/**
 * Created by rajaee050 on 3/19/2018.
 */
public interface Song extends Comparable<Song> {


    /**
     *
     * @return Song's ID
     */
    int getID();

    /**
     *
     * @return Song's Name
     */
    String getName();


    /**
     *
     * @return Song's Length
     */
    int getLength();

    /**
     *
     * @return Singer's name
     */
    String getSingerName();


    /**
     *
     * @param user
     * @param rate
     * @throws User.IllegalRateValue
     * @throws User.SongAlreadyRated
     */
    public void rateSong(User user, int rate) throws User.IllegalRateValue,User.SongAlreadyRated;


    /**
     *
     * @return a collection of users that rated this song ordered as follows:
     *  1- by rating ( from higher to lower )
     *  2- by age ( from lower to higher)
     *  3- by id ( from higher to lower)
     */
    Collection<User> getRaters();

    /**
     *
     * @return a map where keys are the ratings that this song has and each key's value is
     * a Set of Users ( order doesn't matter ) that rated this song with value that equlas
     * th key .
     */
    Map<Integer,Set<User>> getRatings();

    /**
     *
     * @return the average rating of this song
     */
    double getAverageRating();




}
