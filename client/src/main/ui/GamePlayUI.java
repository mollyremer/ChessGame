package ui;

import chess.ChessMove;
import chess.ChessPosition;
import chess.impl.ChessMoveImpl;
import chess.impl.ChessPositionImpl;
import client.Client;
import client.ServerFacade;
import client.WebSocketCommunicator;
import results.ListResult;
import webSocketMessages.userCommands.LeaveCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.ResignCommand;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static client.Client.clientAuthToken;

public class GamePlayUI {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final WebSocketCommunicator webSocket = new WebSocketCommunicator("http://localhost:8080");
    private final ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
    ListResult.GameInformation gameInformation;



    public GamePlayUI(ListResult.GameInformation gameInformation) throws Exception {
        this.gameInformation = gameInformation;
        Scanner scanner = new Scanner(System.in);
        welcomeUI();
        gamePlayScreen(scanner);
    }

    public void gamePlayScreen(Scanner scanner) throws Exception {
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if ((Objects.equals(line, "redraw")) || (Objects.equals(line, "1"))){
                redrawUI(scanner);
            } else if ((Objects.equals(line, "highlight")) || (Objects.equals(line, "2"))){
                highlightUI(scanner);
            } else if ((Objects.equals(line, "move")) || (Objects.equals(line, "3"))){
                getMoveInput(scanner);
            } else if ((Objects.equals(line, "leave")) || (Objects.equals(line, "4"))){
                leaveGameUI(scanner);
            } else if ((Objects.equals(line, "resign")) || (Objects.equals(line, "5"))){
                resignUI(scanner);
            } else if ((Objects.equals(line, "help")) || (Objects.equals(line, "6"))){
                helpUI();
            } else {
                makeMoveUI(scanner, line);
            }
        }
    }

    private void welcomeUI(){
        out.println("Welcome to " + gameInformation.gameName);
//        ChessGame blankChessGame = new ChessGameImpl();
//        new PrintChessBoard(blankChessGame, ChessGame.TeamColor.WHITE);
        out.println("Example of a move: 'mv h2 to h3'");
    }

    private void helpUI(){
        out.println("Type the number to execute the command:");
        out.println("1) redraw chess board");
        out.println("2) highlight legal moves");
        out.println("3) make move (default option)");
        out.println("4) leave the game");
        out.println("5) resign");
        out.println("6) help");
    }

    private void redrawUI(Scanner scanner){

    }



    private void highlightUI(Scanner scanner){}

    private void getMoveInput(Scanner scanner) throws Exception {
        out.println("What's your move?");

        String input = scanner.nextLine();
        makeMoveUI(scanner, input);
    }

    private void makeMoveUI(Scanner scanner, String input) throws Exception {
//        out.println("What's your move?");
//
//        String input = scanner.nextLine();
        input = input.toLowerCase();
        String[] parts = input.split(" ");

        if (parts.length == 4 && parts[0].equals("mv") && parts[1].length() == 2 && parts[2].equals("to") && parts[3].length() == 2){
            String start = parts[1];
            String end = parts[3];

            //int startCol = (int)start.charAt(0) - 96;
            int startCol = convertToInt(start.charAt(0));
            int startRow = (int) start.charAt(1) - 48 ;
            //int endCol = (int)end.charAt(0) - 96;
            int endCol = convertToInt(end.charAt(0));
            int endRow = (int) end.charAt(1) - 48;

            if (!(isValidCoordinate(startCol, startRow) | !(isValidCoordinate(endCol, endRow)))) {
                System.out.println("Please try again. Example of a move: 'mv h2 to h3'");
                gamePlayScreen(scanner);
            }
            ChessPosition startPosition = new ChessPositionImpl(startRow, startCol);
            ChessPosition endPosition = new ChessPositionImpl(endRow, endCol);

            ChessMove move = new ChessMoveImpl(startPosition, endPosition, null);
            MakeMoveCommand moveCommand = new MakeMoveCommand(clientAuthToken.getAuthToken(), gameInformation.getGameID(), move);
            webSocket.sendCommand(moveCommand);
            gamePlayScreen(scanner);
        } else {
            System.out.println("Please try again. Example of a move: 'mv h2 to h3'");
            gamePlayScreen(scanner);
        }

    }

    private int convertToInt(char c){
        switch (c) {
            case 'a' | 'A' -> { return 8; }
            case 'b' | 'B' -> { return 7; }
            case 'c' | 'C' -> { return 6; }
            case 'd' | 'D' -> { return 5; }
            case 'e' | 'E' -> { return 4; }
            case 'f' | 'F' -> { return 3; }
            case 'g' | 'G' -> { return 2; }
            case 'h' | 'H' -> { return 1; }
        }
        return 0;
    }

    private boolean isValidCoordinate(int col, int row){
        return col < 9 && col > 0 && row < 9 && row > 0;
    }

    private void leaveGameUI(Scanner scanner){
        out.println("In leave game");

        LeaveCommand leaveCommand = new LeaveCommand(clientAuthToken.getAuthToken(), gameInformation.getGameID());
        webSocket.sendCommand(leaveCommand);
        new PostLoginUI();
    }

    private void resignUI(Scanner scanner) throws Exception {
        out.println("In resign game");

        ResignCommand resignCommand = new ResignCommand(clientAuthToken.getAuthToken(), gameInformation.getGameID());
        webSocket.sendCommand(resignCommand);
        out.println("Hopefully resigned");
        gamePlayScreen(scanner);
    }

}
