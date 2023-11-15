package services;

import chess.impl.ChessGameImpl;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import requests.CreateRequest;
import results.CreateResult;
import results.DefaultResult;


public class CreateService {
    /**
     * Creates a new game.
     * @param request The request to clear the database.
     * @return The response from the server.
     */
    public CreateResult create(CreateRequest request) throws DataAccessException {
        GameDAO gameDAO = new GameDAO();
        ChessGameImpl gameImpl = new ChessGameImpl();

        if(request.getGameName() == null){
            throw new DataAccessException("Error: bad request");
        }

        Game game = new Game(0, null, null, request.getGameName(), gameImpl);

        gameDAO.insertGame(game);

        return new CreateResult(game.getGameID(), null);
    }
}
