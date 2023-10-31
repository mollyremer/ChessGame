package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import results.DefaultResult;

import static dataAccess.Database.*;

public class ClearService {
    /**
     * Clears the database. Removes all users, games, and authTokens.
     * @return The response from the server.
     */
    public DefaultResult clear(){

        try {
            (new UserDAO()).clear();
            (new AuthDAO()).clear();
            (new GameDAO()).clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return new DefaultResult(null);
    }
}
