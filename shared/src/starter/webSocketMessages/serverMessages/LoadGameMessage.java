package webSocketMessages.serverMessages;

import chess.ChessGame;
import models.Game;

public class LoadGameMessage extends ServerMessage{
    String game;
    ChessGame.TeamColor userColor;
    public LoadGameMessage(ServerMessageType type, String game, ChessGame.TeamColor userColor) {
        super(type);
        this.game = game;
        this.userColor = userColor;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public ChessGame.TeamColor getUserColor() {
        return userColor;
    }

    public void setUserColor(ChessGame.TeamColor userColor) {
        this.userColor = userColor;
    }
}
