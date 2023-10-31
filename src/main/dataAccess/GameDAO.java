package dataAccess;

import chess.ChessGame;
import chess.impl.ChessGameImpl;
import models.Game;

import static dataAccess.Database.dbGames;

public class GameDAO {
    public static int gameCount = 1;
    /**
     * Sets which user is playing as white or black.
     * @param username The username to find a claim a spot for.
     * @param teamColor The team color to assign the user.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void claimSpot(String username, ChessGame.TeamColor teamColor, int gameID) throws DataAccessException{
        if (teamColor == ChessGame.TeamColor.WHITE && findGame(gameID).getWhiteUsername() == null) {
            findGame(gameID).setWhiteUsername(username);
        } else if (teamColor == ChessGame.TeamColor.BLACK && findGame(gameID).getBlackUsername() == null){
            findGame(gameID).setBlackUsername(username);
        } else{
            throw new DataAccessException("Error: already taken");
        }
    }

    /**
     * Inserts a game into the database.
     * @param game The game object to insert.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void insertGame(Game game) throws DataAccessException{
        dbGames.put(game.getGameID(), game);
    }


    /**
     * Finds a game in the database from the gameID.
     * @param gameID the given gameID.
     * @return the requested game.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public Game findGame(int gameID) throws DataAccessException{
        return dbGames.getOrDefault(gameID, null);
    }

    /**
     * Finds all the games in the database.
     * @return all the games in Object[]
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public Object[] findAllGames() throws DataAccessException{
        return dbGames.values().toArray();
    }

    /**
     * Clears all the games.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void clear() throws DataAccessException{
        gameCount = 1;
        dbGames.clear();
    }
}
