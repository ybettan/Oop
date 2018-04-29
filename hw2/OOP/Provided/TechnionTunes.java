package OOP.Provided;

import java.util.Collection;
import java.util.Comparator;

import OOP.Provided.Song.*;
import OOP.Provided.User.*;
import java.util.Set;

/**
 * Created by rajaee050 on 3/19/2018.
 */
public interface TechnionTunes extends Iterable<Song> {

    class UserAlreadyExists extends Exception {}
    class UserDoesntExist extends Exception {}
    class SongAlreadyExists extends Exception {}
    class SongDoesntExist extends Exception {}


    /**
     * adds a new user to the system , if the user already exists a UserAlreadyExists
     * exception should be thrown .
     * @param userID
     * @param userName
     * @param userAge
     */
    void addUser(int userID , String userName , int userAge) throws UserAlreadyExists;
	
	/**
     *
     * @param id : user ID
     * @throws UserDoesntExist if there is no user with such ID number.
	 * @return a reference to the user 
     */
	User getUser(int id) throws UserDoesntExist;

    /**
     *
     * @param id1
     * @param id2
     * @throws AlreadyFriends if they were frineds before.
	 * @throws UserDoesntExist if at least one of the users didn't exist in the system.
     */
    void makeFriends(int id1, int id2) throws  UserDoesntExist , AlreadyFriends , SamePerson;


    /**
     * adds a new song to the system
     * @param songID
     * @param songName
     * @param length
     * @param SingerName
     * @throws SongAlreadyExists if it has been added before
     */
    void addSong(int songID , String songName , int length ,String SingerName) throws SongAlreadyExists ;
	
	/**
     *
     * @param id : song ID
     * @throws SongDoesntExist if there is no song with such ID number.
	 * @return a reference to the song 
     */
	Song getSong(int id) throws SongDoesntExist;

    /**
     *
     * @param userId
     * @param songId
     * @param rate
     * @throws UserDoesntExist
     * @throws SongDoesntExist
     * @throws SongAlreadyRated
     * @throws IllegalRateValue
     */
    void rateSong(int userId, int songId, int rate)throws UserDoesntExist,SongDoesntExist,IllegalRateValue,SongAlreadyRated;


    /**
     *
     * @param IDs : User ID's array
     * @return set ( not ordered ) that contains all the songs that all the given users rated.
     * @throws UserDoesntExist
     */
    Set<Song> getIntersection(int IDs[]) throws UserDoesntExist ;


    /**
     *
     * @param comp
     * @return collection of songs sorted according to comp
     */
    Collection<Song> sortSongs(Comparator<Song> comp);


    /**
     *
     * @param num
     * @return return the most num highest rated songs oredered as folows:
     *  1- by average rating ( from higher to lower )
     *  2- by length (from higher to lower )
     *  3- by ID (from lower to higher )
     */
    Collection<Song> getHighestRatedSongs(int num);

    /**
     *
     * @param num
     * @return the most num rated songs ( songs that users rated most) ordered as follows :
     *  1- by number of ratings ( from higher to lower)
     *  2- by length ( from lower to higher )
     *  3- by ID ( from higher to lower) .
     */
    Collection<Song> getMostRatedSongs(int num);

    /**
     *
     * @param num
     * @return the most num highest users with average rating ordered as follows:
     *  1- by average rating ( from higher to lower)
     *  2- by age ( from higher to lower)
     *  3- by ID ( from lower to higher )
     */
    Collection<User> getTopLikers(int num);

    /**
     *
     * @param userId1
     * @param userId2
	 * @throws UserDoesntExist if at least one of the users didn't exist in the system.
     * @return if the users can be gets alongs together.
     *
     * gets along is defined as follows :
     *  1- every user can get along with himself.
     *  2- two friends can gets along if and only if there exists at least one song that they
     *  both rated 8 or higher .
     *  3- two users that aren't friends can gets along if and only if there exists a path in
     *  the system's friendship graph that every two adjacent friends in the path can gets along.
     *
     */
    boolean canGetAlong(int userId1, int userId2) throws UserDoesntExist;
}
