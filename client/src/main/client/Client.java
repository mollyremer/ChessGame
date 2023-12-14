package client;
import chess.impl.ChessGameImpl;
import com.google.gson.Gson;
import models.AuthToken;
import ui.PreLoginUI;
import ui.PrintChessBoard;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class Client extends Endpoint {
    private Session session;
    public static AuthToken clientAuthToken = new AuthToken();

    public static void main(String[] args) {
        new PreLoginUI();
    }

    public Client() {
        try {
            URI uri = new URI("ws://localhost:8080/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
//                    System.out.println(message);
                    Gson gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()){
                        case ERROR -> {
                            ErrorMessage errorMessage = gson.fromJson(message, ErrorMessage.class);
                            System.out.println(errorMessage.getErrorMessage());
                        }
                        case NOTIFICATION -> {
                            NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
                            System.out.println(notificationMessage.getMessage());
                        }
                        case LOAD_GAME -> {
                            LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);
//                            System.out.println(loadGameMessage.getGame());
                            ChessGameImpl chessGame = new ChessGameImpl(loadGameMessage.getGame());

                            new PrintChessBoard(chessGame, loadGameMessage.getUserColor());
                        }
                    }
                }
            });
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

}
