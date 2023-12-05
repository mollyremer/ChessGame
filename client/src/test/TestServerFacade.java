import chess.ChessGame;
import client.ServerFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.*;
import results.DefaultResult;
import results.ListResult;
import results.LoginResult;
import results.RegisterResult;

import static java.lang.System.exit;
import static java.lang.System.out;

public class TestServerFacade {

    private ServerFacade sf = new ServerFacade("http://localhost:8080");
    public static String user1 = "user1";
    public static String user2 = "user2";

    @BeforeEach
    public void SetUp(){
        sf.clear();
        RegisterRequest request = new RegisterRequest(user1, user1, user1);
        RegisterResult result = sf.register(request);
        out.println();
    }


    @Test
    @DisplayName("Register Positive")
    public void testRegisterPositive() {
        RegisterRequest request = new RegisterRequest(user2, user2, user2);
        RegisterResult result = sf.register(request);
        Assertions.assertNotNull(result, "Should have registered successfully");
    }

    @Test
    @DisplayName("Register Negative")
    public void testRegisterNegative(){
        RegisterRequest request = new RegisterRequest(user1, user1, user1);
        RegisterResult result = sf.register(request);
        Assertions.assertNull(result, "Should have registered unsuccessfully - duplicate registration");
    }

    @Test
    @DisplayName("Login Positive")
    public void testLoginPositive(){
        LoginRequest request = new LoginRequest(user1, user1);
        LoginResult result = sf.login(request);
        Assertions.assertNotNull(result, "Should have logged in successfully");
    }

    @Test
    @DisplayName("Login Negative")
    public void testLoginNegative(){
        LoginRequest request = new LoginRequest(user2, user2);
        LoginResult result = sf.login(request);
        Assertions.assertNull(result, "Should have logged in unsuccessfully - unauthorized");
    }

    @Test
    @DisplayName("Logout Positive")
    public void logOutPositive(){
        DefaultResult result = sf.logout();
        Assertions.assertNotNull(result, "Should have logged out successfully");
    }

    @Test
    @DisplayName("Logout Negative")
    public void logOutNegative(){
        sf.clear();
        DefaultResult result = sf.logout();
        Assertions.assertNull(result, "Should have logged out unsuccessfully - unauthorized");
    }

    @Test
    @DisplayName("Create Game Positive")
    public void createGamePositive(){
        CreateRequest request = new CreateRequest("Test Game Name");
        DefaultResult result = sf.createGame(request);
        Assertions.assertNotNull(result, "Should have created a game successfully");
    }

    @Test
    @DisplayName("Create Game Negative")
    public void createGameNegative(){
        CreateRequest request = new CreateRequest(null);
        DefaultResult result = sf.createGame(request);
        Assertions.assertNull(result, "Should have created a game unsuccessfully - bad request");
    }

    @Test
    @DisplayName("List Positive")
    public void listPositive(){
        CreateRequest request = new CreateRequest("Test Game Name");
        DefaultResult createResult = sf.createGame(request);

        ListResult result = sf.listGames();
        Assertions.assertNotNull(result, "Should have listed successfully");
    }

    @Test
    @DisplayName("List Negative")
    public void listNegative(){
        sf.clear();
        ListResult result = sf.listGames();
        Assertions.assertNull(result, "Should have listed unsuccessfully - unauthorized");
    }

    @Test
    @DisplayName("Join Positive")
    public void joinPositive(){
        CreateRequest createRequest = new CreateRequest("Test Game Name");
        DefaultResult createResult = sf.createGame(createRequest);

        ListResult listResult = sf.listGames();
        String gameNamesString = listResult.getGameNames();
        String[] gameNamesArray = gameNamesString.split(",");
        ListResult.GameInformation gameToJoin = listResult.getGame(gameNamesArray[0]);

        JoinRequest request = new JoinRequest(ChessGame.TeamColor.WHITE, gameToJoin.gameID);
        DefaultResult result = sf.joinGame(request);
        Assertions.assertNotNull(result, "Should have joined successfully");
    }

    @Test
    @DisplayName("Join Positive")
    public void joinN(){
        CreateRequest createRequest = new CreateRequest("Test Game Name");
        DefaultResult createResult = sf.createGame(createRequest);

        ListResult listResult = sf.listGames();
        String gameNamesString = listResult.getGameNames();
        String[] gameNamesArray = gameNamesString.split(",");
        ListResult.GameInformation gameToJoin = listResult.getGame(gameNamesArray[0]);

        JoinRequest request = new JoinRequest(ChessGame.TeamColor.WHITE, 1);
        DefaultResult result = sf.joinGame(request);
        Assertions.assertNull(result, "Should have joined successfully");
    }
}
