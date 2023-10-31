package services;

import chess.impl.ChessGameImpl;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import requests.CreateRequest;
import results.CreateResult;
import results.DefaultResult;

import static dataAccess.GameDAO.gameCount;

public class CreateService {
    /**
     * Creates a new game.
     * @param request The request to clear the database.
     * @return The response from the server.
     */
    public CreateResult create(CreateRequest request) throws DataAccessException {
        GameDAO gameDAO = new GameDAO();
        ChessGameImpl gameImpl = new ChessGameImpl();

        int gameID = gameCount;
        Game game = new Game(gameID, null, null, request.getGameName(), gameImpl);
        gameCount++;

        gameDAO.insertGame(game);

        return new CreateResult(game.getGameID(), null);
    }
}
