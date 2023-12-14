package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.AuthToken;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    private static final ConcurrentHashMap<Integer, ArrayList<Connection>> gameConnections = new ConcurrentHashMap<>();

    public ConnectionHandler(){}

    public void add(Integer gameID, String clientAuthToken, Session session) {
        Connection connection = new Connection(clientAuthToken, session);

        if (gameConnections.containsKey(gameID)) {
            gameConnections.get(gameID).add(connection);
        } else {
            ArrayList<Connection> connections = new ArrayList<>();
            connections.add(connection);
            gameConnections.put(gameID, connections);
        }
    }

    public void remove(Integer gameID, String authToken){
        ArrayList<Connection> connections = gameConnections.get(gameID);
        if (connections != null){
            connections.removeIf(c -> Objects.equals(c.getClientAuthToken(), authToken));
        }
    }

    public void broadcastNotification(Integer gameID, String rootAuthToken, NotificationMessage notification) throws IOException {
        ArrayList<Connection> connections = gameConnections.get(gameID);
        for (Connection c : connections){
            if (!(Objects.equals(c.getClientAuthToken(), rootAuthToken))){
                Gson gson = new Gson();
                c.send(gson.toJson(notification));
            }
        }
    }

    public void loadGameAllConnections(Integer gameID) throws IOException {
        ArrayList<Connection> connections = gameConnections.get(gameID);
        Gson gson = new Gson();
        for (Connection c : connections) {
            try {
                GameDAO gameDAO = new GameDAO();
                AuthDAO authDAO = new AuthDAO();

                Game game = gameDAO.findGame(gameID);
                String g = game.getGame().serialize();

                AuthToken authToken = authDAO.findAuthToken(c.getClientAuthToken());
                ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
                if (Objects.equals(authToken.getUsername(), game.getBlackUsername())) {
                    color = ChessGame.TeamColor.BLACK;
                }

                LoadGameMessage loadGameMessage = null;
                if (color == ChessGame.TeamColor.BLACK) {
                    loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, g, ChessGame.TeamColor.BLACK);
                } else {
                    loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, g, ChessGame.TeamColor.WHITE);
                }
                c.getSession().getRemote().sendString(gson.toJson(loadGameMessage));
            } catch (DataAccessException | IOException e) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
                c.getSession().getRemote().sendString(gson.toJson(errorMessage));
            }
        }
    }

}
