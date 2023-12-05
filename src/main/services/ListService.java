package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import requests.DefaultRequest;
import results.ListResult;

import java.util.ArrayList;
import java.util.Collection;

public class ListService {
    /**
     * Gives a list of all games
     * @param request The request to clear the database.
     * @return The response from the server.
     */
    public ListResult list(DefaultRequest request) throws DataAccessException {
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();

        if(authDAO.findAuthToken(request.getAuthToken()) == null){
            return new ListResult(null, "Error: unauthorized");
        }

        ArrayList<ListResult.GameInformation> gameArray = gameDAO.findAllGames();

        return new ListResult(gameArray, null);
    }
}
