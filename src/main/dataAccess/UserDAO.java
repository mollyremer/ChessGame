package dataAccess;

import models.User;

import java.sql.SQLException;

public class UserDAO {

    private final Database db = new Database();

    /**
     * Inserts a user into the database.
     * @param user The user to insert.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void insertUser(User user) throws DataAccessException {
        var connection = db.getConnection();
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.execute();
        } catch (SQLException e){
            if (e.getMessage().contains("Duplicate entry")){
                throw new DataAccessException("Error: already taken");
            } else {
                throw new DataAccessException(e.getMessage());
            }
        } finally {
            db.closeConnection(connection);
        }
    }

    /**
     * Finds a user from a specified username.
     * @param username The specified username.
     * @return The user object associated with the specified username.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public User findUserByUsername(String username) throws DataAccessException {
        var connection = db.getConnection();
        String sql = "SELECT * FROM users WHERE username = ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // User found
                    return new User(resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"));
                } else {
                    // User not found
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }

//    /**
//     * Updates a specified User object.
//     * @param user The User object to update.
//     * @param newUsername The new username.
//     * @param newPassword The new password.
//     * @param newEmail The new email.
//     * @throws DataAccessException Error thrown when data cannot be accessed.
//     */
//    public void updateUser(User user, String newUsername, String newPassword, String newEmail) throws DataAccessException{
//        if (findUserByUsername(user.getUsername()) == null) { throw new DataAccessException ("Cannot update user, user does not exist"); }
//        else{
//            user.setUsername(newUsername);
//            user.setPassword(newPassword);
//            user.setEmail(newEmail);
//        }
//    }
    //COMMENTED BECAUSE NEVER USED

//    /**
//     * Removes a specified User object.
//     * @param user The User object to remove.
//     * @throws DataAccessException Error thrown when data cannot be accessed.
//     */
//    public void removeUser(User user) throws DataAccessException{
//        if(findUserByUsername(user.getUsername()) == null){
//            throw new DataAccessException("Could not remove user, user does not exist");
//        }
//        else{
//            dbUsers.remove(user.getUsername(), user);
//        }
//    }
    //COMMENTED BECAUSE NEVER USED

    /**
     * Clears all the users.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void clear() throws DataAccessException{
        var connection = db.getConnection();
        String sql = "DELETE FROM users";
        try {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting users: " + e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }
}
