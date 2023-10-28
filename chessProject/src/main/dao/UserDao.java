package dao;

import dataAccess.DataAccessException;
import models.User;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Takes care of all reads and writes to the User database
 */
public class UserDao {
    /**
     * Temporary storage until database is developed
     */
    private static ArrayList<User> users = new ArrayList<User>();

    /**
     * Default constructor intializes storage
     */
    public UserDao() {}

    public void addUser(User user) throws DataAccessException  {
        for (User index : users) {
            if (Objects.equals(user.getUsername(), index.getUsername())) {
                throw new DataAccessException("Error: already taken");
            }
        }
        users.add(user);
    };

    public boolean authenticate(String username, String password) {

        for (User user : users) {
            if ((Objects.equals(user.getUsername(), username)) && (Objects.equals(user.getPassword(), password))) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        users.clear();
    };

    public static ArrayList<User> getUsers() {
        return users;
    }
}
