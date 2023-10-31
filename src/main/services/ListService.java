package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import requests.DefaultRequest;
import requests.LoginRequest;
import results.DefaultResult;
import results.ListResult;

import java.lang.reflect.Array;
import java.util.Collection;

import static dataAccess.Database.dbGames;

public class ListService {
    /**
     * Gives a list of all games
     * @param request The request to clear the database.
     * @return The response from the server.
     */
    public ListResult list(DefaultRequest request) throws DataAccessException {
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();

        if(authDAO.findAuthToken(request.getStrAuthToken()) == null){
            return new ListResult(null, "Error: unauthorized");
        }

        Object[] gameArray = gameDAO.findAllGames();

        return new ListResult(gameArray, null);
    }
}
