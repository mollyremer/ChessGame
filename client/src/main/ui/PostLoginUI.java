package ui;

import chess.ChessGame;
import client.ServerFacade;
import client.WebSocketCommunicator;
import requests.CreateRequest;
import requests.JoinRequest;
import results.DefaultResult;
import results.ListResult;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static client.Client.clientAuthToken;
import static java.lang.System.exit;

public class PostLoginUI {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final WebSocketCommunicator webSocket = new WebSocketCommunicator("http://localhost:8080");
    private final ServerFacade server = new ServerFacade("http://localhost:8080");

    public PostLoginUI(){
        Scanner scanner = new Scanner(System.in);
        loggedInScreen(scanner);
    }

    public void loggedInScreen(Scanner scanner){
        helpUI();

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if ((Objects.equals(line, "logout")) || (Objects.equals(line, "1"))){
                logoutUI(scanner);
            } else if ((Objects.equals(line, "create")) || (Objects.equals(line, "2"))){
                createUI(scanner);
            } else if ((Objects.equals(line, "join")) || (Objects.equals(line, "3"))){
                joinUI(scanner);
            } else if ((Objects.equals(line, "quit")) || (Objects.equals(line, "4"))){
                exit(0);
            } else if ((Objects.equals(line, "help")) || (Objects.equals(line, "5"))){
                helpUI();
            }
        }
    }

    private void helpUI(){
        out.println("Type the number to execute the command:");
        out.println("1) logout");
        out.println("2) create a new game");
        out.println("3) join a game");
        out.println("4) quit");
        out.println("5) help");
    }

    private void logoutUI(Scanner scanner){
        DefaultResult result = server.logout();
        if (result == null || result.getMessage() != null){
            loggedInScreen(scanner);
        } else{
            out.println("Logged out");
            out.println();
            new PreLoginUI();
        }
    }

    private void createUI(Scanner scanner){
        out.println("I hope your day is going so good man what do you want to name your amazing shiny new chess game?");
        String gameName = scanner.nextLine();
        CreateRequest request = new CreateRequest(gameName);
        server.createGame(request);
        loggedInScreen(scanner);
    }

    private void joinUI(Scanner scanner){
        ListResult listResult = server.listGames();

        if (listResult.games == null) {
            out.println("No active games to join");
            loggedInScreen(scanner);
        }

        out.println("Which game would you like to join?");

        for (int i = 0; i < listResult.games.size(); i++) {
            out.println((i + 1) + ") " + listResult.games.get(i).gameName);
            if (listResult.games.get(i).whiteUsername != null){
                out.println("      White user is " + listResult.games.get(i).whiteUsername);
            }
            if (listResult.games.get(i).blackUsername != null){
                out.println("      Black user is " + listResult.games.get(i).blackUsername);
            }
        }

        String gameIndex = scanner.nextLine();

        ChessGame.TeamColor colorToJoin = null;
        out.println("What color would you like to join as? (W)hite, (B)lack, (O)bserver");
        String colorToJoinStr = scanner.nextLine();
        if (Objects.equals(colorToJoinStr, "W") || Objects.equals(colorToJoinStr, "w")) {
            colorToJoin = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(colorToJoinStr, "B") || Objects.equals(colorToJoinStr, "b")) {
            colorToJoin = ChessGame.TeamColor.BLACK;
        } else if (Objects.equals(colorToJoinStr, "O") || Objects.equals(colorToJoinStr, "o")) {
            colorToJoin = null;
        } else {
            out.println("Please enter 'W' to join as white, 'B' to join as black, or 'O' to join as an observer");
            loggedInScreen(scanner);
        }

        try {
            ListResult.GameInformation gameToJoin = listResult.getGame(listResult.games.get(Integer.parseInt(gameIndex) - 1).gameName);
            out.println("Joining " + gameToJoin.getGameName() + " as " + colorToJoinStr);

            JoinRequest joinRequest = new JoinRequest(colorToJoin, gameToJoin.gameID);

            DefaultResult result = server.joinGame(joinRequest);

            if (colorToJoin == null){
                JoinObserverCommand command = new JoinObserverCommand(clientAuthToken.getAuthToken(), gameToJoin.gameID);
                webSocket.sendCommand(command);
            } else{
                JoinPlayerCommand command = new JoinPlayerCommand(clientAuthToken.getAuthToken(), gameToJoin.gameID, colorToJoin);
                webSocket.sendCommand(command);
            }

            if (result == null || result.getMessage() != null){
                loggedInScreen(scanner);
            } else{
                out.println();

                new GamePlayUI(gameToJoin);
            }

        } catch (Exception e){
            out.println("An error occurred, did you enter a valid game number?");
            out.println();
            loggedInScreen(scanner);
        }
    }
}
