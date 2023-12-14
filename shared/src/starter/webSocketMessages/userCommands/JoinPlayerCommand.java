package webSocketMessages.userCommands;

import chess.ChessGame;
public class JoinPlayerCommand extends UserGameCommand {

    ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor){
        super(authToken, gameID);
        this.playerColor = playerColor;
        commandType = CommandType.JOIN_PLAYER;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

}
