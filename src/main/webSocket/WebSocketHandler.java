package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import chess.gsonAdapters.ChessMoveAdapter;
import chess.gsonAdapters.ChessPositionAdapter;
import chess.impl.ChessGameImpl;
import chess.impl.ChessPositionImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import models.AuthToken;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final AuthDAO authDAO = new AuthDAO();
    private final GameDAO gameDAO = new GameDAO();

    private final UserDAO userDAO = new UserDAO();
    private Gson gson;

    public WebSocketHandler() {
    }

    public ServerMessage loadGame(UserGameCommand command){
        try {
            Game game = gameDAO.findGame(command.getGameID());
            String g = game.getGame().serialize();

            AuthToken authToken = authDAO.findAuthToken(command.getAuthString());
            ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
            if (Objects.equals(authToken.getUsername(), game.getBlackUsername())) {
                color = ChessGame.TeamColor.BLACK;
            }

            if (color == ChessGame.TeamColor.BLACK){ return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, g, ChessGame.TeamColor.BLACK);}
            else { return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, g, ChessGame.TeamColor.WHITE); }
        } catch (DataAccessException e){
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }
    }

    public ServerMessage makeMove(MakeMoveCommand command){
        try{
            ConnectionHandler connectionHandler = new ConnectionHandler();
            AuthToken authToken = authDAO.findAuthToken(command.getAuthString());
            Game game = gameDAO.findGame(command.getGameID());
            String username = authToken.getUsername();

            if (game.getState() == 1){ throw new DataAccessException("Error: Cannot make a move when the game is over"); }

            ChessGame.TeamColor teamColor = null;
            if (Objects.equals(game.getBlackUsername(), username)){  teamColor = ChessGame.TeamColor.BLACK; }
            else if (Objects.equals(game.getWhiteUsername(), username)) { teamColor = ChessGame.TeamColor.WHITE; }
            else { throw new DataAccessException("Error: Observer cannot make a move"); }

            if (game.getGame().getTeamTurn() != teamColor){ throw new DataAccessException("Error: Not your turn"); }

            if (game.getGame().isInCheckmate(teamColor)){
                gameDAO.updateField(command.getGameID(), "state", 1);
                throw new DataAccessException("Error: Game is over, checkmate");
            }

            ChessMove move = command.getMove();

            game.getGame().makeMove(move);
            String gameString = game.getGame().serialize();
            gameDAO.updateField(game.getGameID(), "game", gameString);

            checkMateNotifications(game, command, teamColor);

            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, authToken.getUsername() + " moved " + game.getGame().getBoard().getPiece(move.getEndPosition()).getPieceType().toString() + " to " + move.getEndPosition());
        } catch (DataAccessException | InvalidMoveException e){
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }
    }

    public ServerMessage joinPlayer(JoinPlayerCommand command, Session session) {
        try {
            AuthToken authToken = authDAO.findAuthToken(command.getAuthString());
            String color = "";
            if ((command.getPlayerColor() == ChessGame.TeamColor.BLACK)) {
                color = "Black";
                if (!(Objects.equals(authToken.getUsername(), gameDAO.findGame(command.getGameID()).getBlackUsername()))){
                    throw new DataAccessException("Error: joining as wrong color");
                }
            } else if ((command.getPlayerColor() == ChessGame.TeamColor.WHITE)) {
                color = "White";
                if (!(Objects.equals(authToken.getUsername(), gameDAO.findGame(command.getGameID()).getWhiteUsername()))){
                    throw new DataAccessException("Error: joining as wrong color");
                }
            }
            ConnectionHandler connectionHandler = new ConnectionHandler();
            connectionHandler.add(command.getGameID(), command.getAuthString(), session);
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, authToken.getUsername() + " has joined the game as " + color);
        } catch (DataAccessException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }
    }

    public ServerMessage joinObserver(JoinObserverCommand command, Session session) {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connectionHandler.add(command.getGameID(), command.getAuthString(), session);

        try {
            AuthToken authToken = authDAO.findAuthToken(command.getAuthString());
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, authToken.getUsername() + " has joined the game as an Observer");
        } catch (DataAccessException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }
    }

    public ServerMessage leave(LeaveCommand command, Session session) {
        try {
            AuthToken authToken = authDAO.findAuthToken(command.getAuthString());
            Game game = gameDAO.findGame(command.getGameID());

            if (Objects.equals(game.getWhiteUsername(), authToken.getUsername())) {
                gameDAO.updateField(command.getGameID(), "whiteUsername", null);
            }
            if (Objects.equals(game.getBlackUsername(), authToken.getUsername())) {
                gameDAO.updateField(command.getGameID(), "blackUsername", null);
            }

            new ConnectionHandler().remove(command.getGameID(), command.getAuthString());
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, authToken.getUsername() + " has left the game.");
        } catch (DataAccessException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }
    }

    public ServerMessage resign(ResignCommand command) {
        try {
            AuthToken authToken = authDAO.findAuthToken(command.getAuthString());
            Game game = gameDAO.findGame(command.getGameID());

            if (Objects.equals(game.getWhiteUsername(), authToken.getUsername()) || Objects.equals(game.getBlackUsername(), authToken.getUsername())) {
                gameDAO.updateField(command.getGameID(), "state", 1);
            } else {
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: An observer cannot resign");
            }

            if (game.getState() == 1){
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Cannot resign when the game is over");
            }

            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, authToken.getUsername() + " has resigned.");
        } catch (DataAccessException e){
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }
    }

    public void checkMateNotifications(Game game, UserGameCommand command, ChessGame.TeamColor teamColor) {
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();

            NotificationMessage checkMateMessage = null;
            if (game.getGame().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                checkMateMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, game.getWhiteUsername() + " is in checkmate, " + game.getBlackUsername() + " wins!");
            }
            if (game.getGame().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                checkMateMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, game.getBlackUsername() + " is in checkmate, " + game.getWhiteUsername() + " wins!");
            }
            if (checkMateMessage != null) {
                connectionHandler.broadcastNotification(command.getGameID(), null, checkMateMessage);
                gameDAO.updateField(command.getGameID(), "state", 1);
                return;
            }

            if (teamColor == ChessGame.TeamColor.WHITE) {
                if (game.getGame().isInCheck(ChessGame.TeamColor.BLACK)) {
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, game.getBlackUsername() + " is in check");
                    connectionHandler.broadcastNotification(command.getGameID(), null, checkMateMessage);
                }
            }
            if (teamColor == ChessGame.TeamColor.BLACK) {
                if (game.getGame().isInCheck(ChessGame.TeamColor.WHITE)) {
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, game.getWhiteUsername() + " is in check");
                    connectionHandler.broadcastNotification(command.getGameID(), null, checkMateMessage);
                }
            }
        } catch (IOException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        ConnectionHandler connectionHandler = new ConnectionHandler();

        System.out.printf("Received: %s", message);

        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessMove.class, new ChessMoveAdapter());
        builder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
        gson = builder.create();

        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

        if (gameDAO.findGame(command.getGameID()) == null) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Game ID does not exist")));
            return;
        }

        if (authDAO.findAuthToken(command.getAuthString()) == null){
            session.getRemote().sendString(gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Auth token does not exist")));
        }

        ServerMessage broadCast = null;
        ServerMessage loadGameMessage = null;
        switch (command.getCommandType()) {
            case LEAVE -> {
                LeaveCommand leaveCommand = gson.fromJson(message, LeaveCommand.class);
                broadCast = leave(leaveCommand, session);
            }
            case RESIGN -> {
                ResignCommand resignCommand = gson.fromJson(message, ResignCommand.class);
                ServerMessage resignMessage = resign(resignCommand);
                if (resignMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                    connectionHandler.broadcastNotification(command.getGameID(), null, ((NotificationMessage) resignMessage));
                } else {
                    broadCast = resignMessage;
                }
            }
            case JOIN_PLAYER -> {
                JoinPlayerCommand joinPlayerCommand = gson.fromJson(message, JoinPlayerCommand.class);
                broadCast = joinPlayer(joinPlayerCommand, session);
                loadGameMessage = loadGame(command);
            }
            case JOIN_OBSERVER -> {
                JoinObserverCommand joinObserverCommand = gson.fromJson(message, JoinObserverCommand.class);
                broadCast = joinObserver(joinObserverCommand, session);
                loadGameMessage = loadGame(command);
            }
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = gson.fromJson(message, MakeMoveCommand.class);
                broadCast = makeMove(makeMoveCommand);
                if (broadCast.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                    connectionHandler.loadGameAllConnections(command.getGameID());
                }
            }
        }


        if (broadCast != null) {
            switch (broadCast.getServerMessageType()) {
                case ERROR -> session.getRemote().sendString(gson.toJson((ErrorMessage) broadCast));
                case NOTIFICATION -> {
                    connectionHandler.broadcastNotification(command.getGameID(), command.getAuthString(), ((NotificationMessage) broadCast));
                    if (loadGameMessage != null) {
                        switch (loadGameMessage.getServerMessageType()) {
                            case ERROR -> session.getRemote().sendString(gson.toJson((ErrorMessage) loadGameMessage));
                            case LOAD_GAME -> session.getRemote().sendString(gson.toJson(loadGameMessage));
                        }
                    }
                }
            }
        }

    }
}
