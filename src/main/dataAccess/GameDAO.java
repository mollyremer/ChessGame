package dataAccess;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import chess.impl.ChessGameImpl;
import models.Game;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static java.lang.Math.abs;

public class GameDAO {
    private final Database db = new Database();

    /**
     * Sets which user is playing as white or black.
     * @param username The username to find a claim a spot for.
     * @param teamColor The team color to assign the user.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void claimSpot(String username, ChessGame.TeamColor teamColor, int gameID) throws DataAccessException{
        if (teamColor == null){
            return;
        }
        var connection = db.getConnection();
        if(findGame(gameID) == null){
            throw new DataAccessException("Error: bad request");
        }
        if(teamColor == ChessGame.TeamColor.WHITE){
            if (findGame(gameID).getWhiteUsername() != null){
                throw new DataAccessException("Error: already taken");
            }
        }
        if(teamColor == ChessGame.TeamColor.BLACK){
            if (findGame(gameID).getBlackUsername() != null){
                throw new DataAccessException("Error: already taken");
            }
        }
        String sql = "UPDATE games SET " + (teamColor == ChessGame.TeamColor.WHITE ? "whiteUsername":"blackUsername") + " = ? WHERE gameID = ?";
        try (var preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, gameID);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }

    /**
     * Inserts a game into the database.
     * @param game The game object to insert.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void insertGame(Game game) throws DataAccessException{
        if (game == null){
            throw new DataAccessException("Error: null game");
        }
        var connection = db.getConnection();
        String sql = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            int specialGameID = abs(UUID.randomUUID().hashCode());
            game.setGameID(specialGameID);
            preparedStatement.setInt(1, specialGameID);
            preparedStatement.setString(2, game.getWhiteUsername());
            preparedStatement.setString(3, game.getBlackUsername());
            preparedStatement.setString(4, game.getGameName());

            String gameString = game.getGame().serialize();
            preparedStatement.setString(5, gameString);
            preparedStatement.executeUpdate();
            var result = preparedStatement.getGeneratedKeys();
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }

    /**
     * Finds a game in the database from the gameID.
     * @param gameID the given gameID.
     * @return the requested game.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public Game findGame(int gameID) throws DataAccessException {
        var connection = db.getConnection();
        String sql = "SELECT * FROM games WHERE gameID = ?";
        try (var preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, gameID);
            try (var resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    ChessGameImpl game = new ChessGameImpl(resultSet.getString("game"));
                    return new Game(resultSet.getInt("gameID"),
                            resultSet.getString("whiteUsername"),
                            resultSet.getString("blackUsername"),
                            resultSet.getString("gameName"),
                            game);
                }
            } catch (SQLException e) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding game");
        }
        return null;
    }

    /**
     * Finds all the games in the database.
     * @return all the games in Object[]
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public Object[] findAllGames() throws DataAccessException{
        Collection<Game> games = new HashSet<>();
        var connection = db.getConnection();
        String sql = "SELECT * FROM games";
        try (var preparedStatement = connection.prepareStatement(sql)){
            try (var resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    //FIXME: deserialize
//                    var game = gsonBuilder.create().fromJson(resultSet.getString("game"), ChessGameImpl.class);
                    ChessGameImpl game = new ChessGameImpl(resultSet.getString("game"));
                    games.add(new Game(resultSet.getInt("gameID"),
                                        resultSet.getString("whiteUsername"),
                                        resultSet.getString("blackUsername"),
                                        resultSet.getString("gameName"),
                                        game));
                }
                return games.toArray();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Clears all the games.
     * @throws DataAccessException Error thrown when data cannot be accessed.
     */
    public void clear() throws DataAccessException{
        var connection = db.getConnection();
        String sql = "DELETE FROM games";
        try {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting games: " + e.getMessage());
        } finally {
            db.closeConnection(connection);
        }
    }
}
