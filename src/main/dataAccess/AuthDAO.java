package dataAccess;

import models.AuthToken;

import java.sql.SQLException;

public class AuthDAO {
    private final Database db = new Database();

    /**
     * Inserts an authToken into the database.
     * @param authTokenObj to insert.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void insertAuthToken(AuthToken authTokenObj) throws DataAccessException{
        var connection = db.getConnection();
        String sql = "INSERT INTO auth_tokens (authToken, username) VALUES (? ,?)";
        try (var preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, authTokenObj.getAuthToken());
            preparedStatement.setString(2, authTokenObj.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }

    /**
     * Finds an AuthToken object based on an authToken string
     * @param authToken the string to find the authToken object with.
     * @return the AuthToken object.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public AuthToken findAuthToken(String authToken) throws DataAccessException{
        var connection = db.getConnection();
        String sql = "SELECT * FROM auth_tokens WHERE authToken = ?";
        try (var preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, authToken);
            try (var resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return new AuthToken(resultSet.getString("authToken"),
                            resultSet.getString("username"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }

    /**
     * Removes the specified AuthToken object.
     * @param authTokenObj the AuthToken to remove.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void removeAuthToken(AuthToken authTokenObj) throws DataAccessException{
        var connection = db.getConnection();
        String sql = "DELETE FROM auth_tokens WHERE authToken = ?";
        try (var preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, authTokenObj.getAuthToken());
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted == 0){
                throw new DataAccessException("AuthToken doesn't exist in the db");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting AuthToken: " + e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }


    /**
     * Clears all AuthToken objects.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void clear() throws DataAccessException{
        var connection = db.getConnection();
        String sql = "DELETE FROM auth_tokens";
        try {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting authTokens: " + e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }

}
