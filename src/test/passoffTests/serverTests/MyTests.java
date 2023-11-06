package passoffTests.serverTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import requests.*;
import results.*;
import services.*;

import static dataAccess.TreeDatabase.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyTests {

    @Nested
    class RegisterServiceTests {
        private final RegisterService service = new RegisterService();

        @Test
        @Order(1)
        @DisplayName("Positive Register a User")
        public void successRegister() throws Exception {
            ClearService clearService = new ClearService();
            clearService.clear();

            RegisterRequest request = new RegisterRequest("user1", "password1", "email1");
            RegisterResult result = service.register(request);

            Assertions.assertNull(result.getMessage(), "There shouldn't be an error message");
            Assertions.assertEquals(1, dbUsers.size(), "There should be 1 registered user");
        }

        @Test
        @Order(2)
        @DisplayName("Negative Register a User")
        public void failRegister() throws DataAccessException {
            ClearService clearService = new ClearService();
            clearService.clear();

            RegisterRequest request = new RegisterRequest(null, "password2", "email2");
            RegisterResult result = service.register(request);

            Assertions.assertEquals(result.getMessage(), "Error: bad request");
            Assertions.assertEquals(0, dbUsers.size(), "User should not have been registered");
        }

    }

    @Nested
    class LoginServiceTests {

        private final LoginService service = new LoginService();

        @BeforeAll
        public static void setup() throws DataAccessException {
            ClearService clearService = new ClearService();
            clearService.clear();

            RegisterService service = new RegisterService();
            RegisterRequest registerRequest = new RegisterRequest("user1", "password1", "email1");
            RegisterResult registerResult = service.register(registerRequest);
        }

        @Test
        @Order(3)
        @DisplayName("Positive User Login")
        public void successLogin() throws DataAccessException {
            LoginRequest loginRequest = new LoginRequest("user1", "password1", null);
            LoginResult loginResult = service.login(loginRequest);

            Assertions.assertNull(loginResult.getMessage(), "There shouldn't be an error message");
            Assertions.assertEquals(2, dbAuthTokens.size(), "There should be an authToken for the user");
        }


        @Test
        @Order(4)
        @DisplayName("Negative User Login")
        public void negativeLogin() throws DataAccessException {
            LoginRequest loginRequest = new LoginRequest("user1", "badPassword", null);
            LoginResult loginResult = service.login(loginRequest);

            Assertions.assertEquals("Error: unauthorized", loginResult.getMessage(), "There should be an error message");
            Assertions.assertEquals(2, dbAuthTokens.size(), "There shouldn't be a third authToken for the user");
        }
    }

    @Nested
    class LogoutServiceTests {

        private final LogoutService service = new LogoutService();

        @BeforeAll
        public static void setup() throws DataAccessException {
            ClearService clearService = new ClearService();
            clearService.clear();

            RegisterService registerService = new RegisterService();
            RegisterRequest registerRequest = new RegisterRequest("user1", "password1", "email1");
            RegisterResult registerResult = registerService.register(registerRequest);
        }

        @Test
        @Order(5)
        @DisplayName("Positive User Logout")
        public void positiveLogout() throws DataAccessException {
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new LoginRequest("user1", "password1", null);
            LoginResult loginResult = loginService.login(loginRequest);
            String strAuthToken = loginResult.getAuthToken();

            DefaultRequest logoutRequest = new DefaultRequest(strAuthToken);
            DefaultResult logoutResult = service.logout(logoutRequest);

            Assertions.assertNull(logoutResult.getMessage(), "There shouldn't be an error message");
            Assertions.assertFalse(dbAuthTokens.containsKey(strAuthToken), "The user shouldn't be logged in");
        }

        @Test
        @Order(6)
        @DisplayName("Negative User Logout")
        public void negativeLogout() {
            String strAuthToken = "fakeAuthToken";

            DefaultRequest logoutRequest = new DefaultRequest(strAuthToken);
            DefaultResult logoutResult = service.logout(logoutRequest);

            Assertions.assertEquals("Error: unauthorized", logoutResult.getMessage(), "There should be an error message");
            Assertions.assertFalse(dbAuthTokens.containsKey(strAuthToken), "The user shouldn't be logged in");
        }
    }

    @Nested
    class CreateServiceTests {

        private final CreateService service = new CreateService();

        @BeforeAll
        public static void setup() throws DataAccessException {
            ClearService clearService = new ClearService();
            clearService.clear();

            RegisterService registerService = new RegisterService();
            RegisterRequest registerRequest = new RegisterRequest("user1", "password1", "email1");
            RegisterResult registerResult = registerService.register(registerRequest);
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new LoginRequest("user1", "password1", null);
            LoginResult loginResult = loginService.login(loginRequest);
            String strAuthToken = loginResult.getAuthToken();
        }

        @Test
        @Order(7)
        @DisplayName("Positive Create Game")
        public void positiveCreateGame() throws DataAccessException {
            String gameName = "gameName1";
            CreateRequest createRequest = new CreateRequest(gameName, null);
            CreateResult createResult = service.create(createRequest);

            Assertions.assertNull(createResult.getMessage(), "There shouldn't be an error message");
            Assertions.assertEquals(2, createResult.getGameID(), "The gameID is in an unexpected form");
            Assertions.assertTrue(dbGames.containsKey(1), "The game wasn't inserted into the database");
        }

        @Test
        @Order(8)
        @DisplayName("Negative Create Game")
        public void negativeCreateGame() throws DataAccessException {
            String gameName = "gameName1";
            CreateRequest createRequest = new CreateRequest(gameName, null);
            CreateResult createResult = service.create(createRequest);

            Assertions.assertNull(createResult.getMessage(), "There shouldn't be an error message");
            Assertions.assertEquals(1, createResult.getGameID(), "The gameID is in an unexpected form");
            Assertions.assertTrue(dbGames.containsKey(1), "The game wasn't inserted into the database");
        }
    }

    @Nested
    class JoinGameService {
        private final JoinService service = new JoinService();
        private static String strAuthToken = "";

        @BeforeAll
        public static void setup() throws DataAccessException {
            ClearService clearService = new ClearService();
            clearService.clear();

            RegisterService registerService = new RegisterService();
            RegisterRequest registerRequest = new RegisterRequest("user1", "password1", "email1");
            RegisterResult registerResult = registerService.register(registerRequest);
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new LoginRequest("user1", "password1", null);
            LoginResult loginResult = loginService.login(loginRequest);
            strAuthToken = loginResult.getAuthToken();
            CreateService createService = new CreateService();
            String gameName = "gameName1";
            CreateRequest createRequest = new CreateRequest(gameName, null);
            CreateResult createResult = createService.create(createRequest);
        }

        @Test
        @Order(9)
        @DisplayName("Positive Join Game")
        public void positiveJoinGame () throws DataAccessException {
            ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
            JoinRequest joinRequest = new JoinRequest(teamColor, 1, strAuthToken);
            DefaultResult joinResult = service.join(joinRequest);

            Assertions.assertNull(joinResult.getMessage(), "There shouldn't be an error message");
            Assertions.assertEquals("user1", dbGames.get(1).getWhiteUsername(), "User wasn't given white team color");
        }

        @Test
        @Order(10)
        @DisplayName("Negative Join Game")
        public void negativeJoinGame () throws DataAccessException {
            ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
            JoinRequest joinRequest = new JoinRequest(teamColor, 5, strAuthToken);
            DefaultResult joinResult = service.join(joinRequest);

            Assertions.assertEquals("Error: bad request", joinResult.getMessage(), "Shouldn't be able to join a game that doesn't exist");
            Assertions.assertNull(dbGames.get(1).getWhiteUsername(), "Nobody should be the white player");
        }
    }

    @Nested
    class ListServiceTests {
        private final ListService service = new ListService();
        private static String strAuthToken = "";

        @BeforeAll
        public static void setup() throws DataAccessException {
            ClearService clearService = new ClearService();
            clearService.clear();

            RegisterService registerService = new RegisterService();
            RegisterRequest registerRequest = new RegisterRequest("user1", "password1", "email1");
            RegisterResult registerResult = registerService.register(registerRequest);
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new LoginRequest("user1", "password1", null);
            LoginResult loginResult = loginService.login(loginRequest);
            strAuthToken = loginResult.getAuthToken();
            CreateService createService = new CreateService();
            String gameName = "gameName1";
            CreateRequest createRequest = new CreateRequest(gameName, null);
            CreateResult createResult = createService.create(createRequest);
            String gameName2 = "gameName2";
            CreateRequest createRequest2 = new CreateRequest(gameName2, null);
            CreateResult createResult2 = createService.create(createRequest2);
        }

        @Test
        @Order(11)
        @DisplayName("Positive List Game")
        public void positiveListGame() throws DataAccessException {
            DefaultRequest listRequest = new DefaultRequest(strAuthToken);
            ListResult listResult = service.list(listRequest);

            Assertions.assertNull(listResult.getMessage(), "Shouldn't have an error message");
            Assertions.assertEquals(2, dbGames.size(), "Should have two games in the database");
        }

        @Test
        @Order(12)
        @DisplayName("Negative List Game")
        public void negativeListGame() throws DataAccessException {
            DefaultRequest listRequest = new DefaultRequest("badAuthToken");
            ListResult listResult = service.list(listRequest);

            Assertions.assertEquals("Error: unauthorized", listResult.getMessage(), "Should have an error message");
            Assertions.assertEquals(2, dbGames.size(), "Should have two games in the database");
        }
    }

    @Test
    @Order(13)
    @DisplayName("Clear")
    public void clear(){
        ClearService clearService = new ClearService();
        clearService.clear();

        Assertions.assertEquals(0, dbGames.size(), "Games database isn't 0");
        Assertions.assertEquals(0, dbUsers.size(), "Users database isn't 0");
        Assertions.assertEquals(0, dbAuthTokens.size(), "Auth database isn't 0");
    }




}
