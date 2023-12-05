package services;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.AuthToken;
import requests.JoinRequest;
import results.DefaultResult;

public class JoinService {
    /**
     * Verifies that the specified game exists, and, if a color is specified, adds the caller
     * as the requested color to the game. If no color is specified the user is joined as an
     * observer. This request is idempotent.
     * @param request The request to clear the database.
     * @return The response from the server.
     */
    public DefaultResult join(JoinRequest request) throws DataAccessException {
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        ChessGame.TeamColor playercolor;
        int gameID;


        try {
            playercolor = request.getPlayerColor();
            gameID = request.getGameID();
        } catch (Exception e) {
            return new DefaultResult("Error: bad request");
        }

        if (gameDAO.findGame(gameID) == null){
            return new DefaultResult("Error: bad request");
        }

        if (authDAO.findAuthToken(request.getAuthToken()) == null){
            return new DefaultResult("Error: unauthorized");
        }

        if (playercolor != null){
            try {
                AuthToken objAuthToken = authDAO.findAuthToken(request.getAuthToken());
                gameDAO.claimSpot(objAuthToken.getUsername(), playercolor, gameID);
            } catch (DataAccessException e){
                return new DefaultResult(e.getMessage());
            }
        }


        return new DefaultResult(null);
    }
}
