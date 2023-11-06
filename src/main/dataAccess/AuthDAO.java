package dataAccess;

import models.AuthToken;

import java.util.UUID;

import static dataAccess.TreeDatabase.dbAuthTokens;

public class AuthDAO {
    /**
     * Generates a random authTokens.
     * @return an authToken string.
     */
    public String generateAuthTokenStr() {
        return UUID.randomUUID().toString();
    }

    /**
     * Inserts an authToken into the database.
     * @param authTokenObj to insert.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void insertAuthToken(AuthToken authTokenObj) throws DataAccessException{
        dbAuthTokens.put(authTokenObj.getAuthToken(), authTokenObj);
    }

    /**
     * Finds an AuthToken object based on an authToken string
     * @param authToken the string to find the authToken object with.
     * @return the AuthToken object.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public AuthToken findAuthToken(String authToken) throws DataAccessException{
        return dbAuthTokens.getOrDefault(authToken, null);
    }

    /**
     * Removes the specified AuthToken object.
     * @param authTokenObj the AuthToken to remove.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void removeAuthToken(AuthToken authTokenObj) throws DataAccessException{
        if(findAuthToken(authTokenObj.getAuthToken()) == null){
            throw new DataAccessException("Could not remove auth, does not exist");
        }
        else {
            dbAuthTokens.remove(authTokenObj.getAuthToken(), authTokenObj);
        }
    }


    /**
     * Clears all AuthToken objects.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void clear() throws DataAccessException{
        dbAuthTokens.clear();
    }

}
