import chess.ChessGame;
import chess.impl.ChessGameImpl;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.*;
import results.ListResult;

import java.util.Collection;
import java.util.UUID;

public class MyDatabaseTests {

    public UserDAO userDAO = new UserDAO();
    public AuthDAO authDAO = new AuthDAO();
    public GameDAO gameDAO = new GameDAO();

    @BeforeEach
    public void SetUp() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

        User initialUser1 = new User("initialUser1", "initialPassword1", "initialEmail1");
        userDAO.insertUser(initialUser1);

        User initialUser2 = new User("initialUser2", "initialPassword2", "initialEmail2");
        userDAO.insertUser(initialUser2);
    }

    @Test
    @DisplayName("Registers a user")
    public void insertUser() throws DataAccessException {
        User expectedUser = new User("user1", "password1", "email1");
        userDAO.insertUser(expectedUser);

        Assertions.assertEquals(expectedUser, userDAO.findUserByUsername("user1"), "Expected user could not be found in the database");
    }

    @Test
    @DisplayName("Tries to register an existing initial user")
    public void insertExistingUser() throws DataAccessException {
        User initialUser1 = new User("initialUser1", "initialPassword1", "initialEmail1");
        try{
            userDAO.insertUser(initialUser1);
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: already taken", e.getMessage(), "There should be an error message");
        }

        Assertions.assertEquals(initialUser1, userDAO.findUserByUsername("initialUser1"), "Expected user could not be found in the database");
    }

    @Test
    @DisplayName("Finds user by the username")
    public void findUserByUsername() throws DataAccessException {
        User initialUser1 = new User("initialUser1", "initialPassword1", "initialEmail1");
        Assertions.assertEquals(initialUser1, userDAO.findUserByUsername("initialUser1"), "Expected user could not be found in the database");
    }

    @Test
    @DisplayName("Tries to find a non-existent user by the username")
    public void findNonexistentUser() throws DataAccessException {
        Assertions.assertNull(userDAO.findUserByUsername("non-existentUser"), "Should have returned null, user does not exist");
    }

    @Test
    @DisplayName("Clear users")
    public void clearUsers() throws DataAccessException {
        User expectedUser = new User("user1", "password1", "email1");
        userDAO.insertUser(expectedUser);

        Assertions.assertEquals(expectedUser, userDAO.findUserByUsername("user1"), "Expected user could not be found");

        userDAO.clear();

        Assertions.assertNull(userDAO.findUserByUsername("initialUser1"), "Expected to not find non-existent user");
    }

    @Test
    @DisplayName("Insert AuthToken")
    public void insertAuthToken() throws DataAccessException {
        User expectedUser = new User("user1", "password1", "email1");
        userDAO.insertUser(expectedUser);

        String strAuthToken = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(strAuthToken, "user1");

        authDAO.insertAuthToken(authToken);

        Assertions.assertEquals(authToken, authDAO.findAuthToken(strAuthToken), "Couldn't find the authToken in the database");
    }

    @Test
    @DisplayName("Finds authToken")
    public void findAuthToken() throws DataAccessException {
        String strAuthToken = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(strAuthToken, "initialUser1");

        authDAO.insertAuthToken(authToken);

        Assertions.assertEquals(authToken, authDAO.findAuthToken(strAuthToken), "Found object was not expected");
    }

    @Test
    @DisplayName("Finds nonexistent authToken")
    public void findNonexistentAuthToken() throws DataAccessException {
        String strAuthToken = UUID.randomUUID().toString();

        Assertions.assertNull(authDAO.findAuthToken(strAuthToken), "Should've returned null, object dne");
    }

    @Test
    @DisplayName("Removed a specified authToken")
    public void removeSpecificAuthToken() throws DataAccessException {
        String strAuthToken = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(strAuthToken, "initialUser1");

        authDAO.insertAuthToken(authToken);

        Assertions.assertEquals(authToken, authDAO.findAuthToken(strAuthToken), "Found object was not expected");

        authDAO.removeAuthToken(authToken);

        Assertions.assertNull(authDAO.findAuthToken(strAuthToken), "AuthToken was not removed correctly");
    }

    @Test
    @DisplayName("Clear auth")
    public void clearAuthTokens() throws DataAccessException {
        String strAuthToken1 = UUID.randomUUID().toString();
        AuthToken authToken1 = new AuthToken(strAuthToken1, "initialUser1");
        String strAuthToken2 = UUID.randomUUID().toString();
        AuthToken authToken2 = new AuthToken(strAuthToken2, "initialUser2");

        authDAO.insertAuthToken(authToken1);
        authDAO.insertAuthToken(authToken2);

        Assertions.assertEquals(authToken1, authDAO.findAuthToken(strAuthToken1), "Found object was not expected");
        Assertions.assertEquals(authToken2, authDAO.findAuthToken(strAuthToken2), "Found object was not expected");

        authDAO.clear();

        Assertions.assertNull(authDAO.findAuthToken(strAuthToken1), "AuthToken was not removed correctly");
        Assertions.assertNull(authDAO.findAuthToken(strAuthToken2), "AuthToken was not removed correctly");
    }

    @Test
    @DisplayName("Insert Game")
    public void insertGame() throws DataAccessException {
        ChessGameImpl chessGame = new ChessGameImpl();
        Game game = new Game(1, null, null, "gameName", chessGame);
        gameDAO.insertGame(game);

        Assertions.assertNotNull(gameDAO.findGame(game.getGameID()), "Game could not be found after inserting");
    }

    @Test
    @DisplayName("Insert Null Game")
    public void insertNullGame() {
        Game game = null;
        try {
            gameDAO.insertGame(game);
            Assertions.assertNotNull(gameDAO.findGame(game.getGameID()), "Game should not be found after bad inserting");
        } catch (DataAccessException e) {
            Assertions.assertNotNull(e.getMessage(), "Should have an error message");
        }
    }

    @Test
    @DisplayName("Find Game")
    public void findGame() throws DataAccessException {
        ChessGameImpl chessGame = new ChessGameImpl();
        Game game = new Game(1, null, null, "gameName", chessGame);
        gameDAO.insertGame(game);

        Assertions.assertNotNull(gameDAO.findGame(game.getGameID()), "Game could not be found");
    }

    @Test
    @DisplayName("Find Null Game")
    public void findNullGame(){
        Game game = null;
        try {
            gameDAO.insertGame(game);
            Assertions.assertNotNull(gameDAO.findGame(game.getGameID()), "Game should not be found after bad inserting");
        } catch (DataAccessException e) {
            Assertions.assertNotNull(e.getMessage(), "Should have an error message");
        }
    }

    @Test
    @DisplayName("Find All Games")
    public void findAllGames() throws DataAccessException {
        gameDAO.insertGame(new Game(0, null, null, "game1", new ChessGameImpl()));
        gameDAO.insertGame(new Game(0, null, null, "game2", new ChessGameImpl()));

        Collection<ListResult.GameInformation> games = gameDAO.findAllGames();

        Assertions.assertNotNull(games, "There should be a list of games");
        Assertions.assertEquals(2, games.size(), "Should have 2 games");
    }

    @Test
    @DisplayName("Claim spot")
    public void claimSpot() throws DataAccessException {
        Game game = new Game(1, null, null, "gameName", new ChessGameImpl());
        gameDAO.insertGame(game);

        gameDAO.claimSpot("user1", ChessGame.TeamColor.BLACK, game.getGameID());
        Assertions.assertEquals(gameDAO.findGame(game.getGameID()).getBlackUsername() , "user1", "The black username should be user1");
    }

    @Test
    @DisplayName("Claim spot that's already been claimed")
    public void claimSpotAlreadyClaimed() throws DataAccessException {
        Game game = new Game(1, null, null, "gameName", new ChessGameImpl());
        gameDAO.insertGame(game);

        gameDAO.claimSpot("user1", ChessGame.TeamColor.BLACK, game.getGameID());
        Assertions.assertEquals(gameDAO.findGame(game.getGameID()).getBlackUsername() , "user1", "The black username should be user1");

        try {
            gameDAO.claimSpot("user2", ChessGame.TeamColor.BLACK, game.getGameID());
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: already taken", "The black player is already taken");
        }
        Assertions.assertEquals(gameDAO.findGame(game.getGameID()).getBlackUsername() , "user1", "The black username should be user1");
    }

}
