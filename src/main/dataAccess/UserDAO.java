package dataAccess;

import models.Game;
import models.User;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import static dataAccess.Database.dbGames;
import static dataAccess.Database.dbUsers;

public class UserDAO {

    /**
     * Inserts a user into the database.
     * @param user The user to insert.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void insertUser(User user) throws DataAccessException {
        dbUsers.put(user.getUsername(), user);
    }

    /**
     * Finds a user from a specified username.
     * @param username The specified username.
     * @return The user object associated with the specified username.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public User findUser(String username) throws DataAccessException {
        return dbUsers.getOrDefault(username, null);
    }

    /**
     * Updates a specified User object.
     * @param user The User object to update.
     * @param newUsername The new username.
     * @param newPassword The new password.
     * @param newEmail The new email.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void updateUser(User user, String newUsername, String newPassword, String newEmail) throws DataAccessException{
        if (findUser(user.getUsername()) == null) { throw new DataAccessException ("Cannot update user, user does not exist"); }
        else{
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            user.setEmail(newEmail);
        }
    }

    /**
     * Removes a specified User object.
     * @param user The User object to remove.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void removeUser(User user) throws DataAccessException{
        if(findUser(user.getUsername()) == null){
            throw new DataAccessException("Could not remove user, user does not exist");
        }
        else{
            dbUsers.remove(user.getUsername(), user);
        }
    }

    /**
     * Clears all the users.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void clear() throws DataAccessException{
        dbUsers.clear();
    }

}
