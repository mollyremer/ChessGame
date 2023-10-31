package requests;

import chess.ChessGame;

public class JoinRequest {
    ChessGame.TeamColor playerColor;
    int gameID;

    String strAuthToken;

    public JoinRequest() {}

    public String getStrAuthToken() {
        return strAuthToken;
    }

    public void setStrAuthToken(String strAuthToken) {
        this.strAuthToken = strAuthToken;
    }

    /**
     * Constructs a JoinRequest with the provided playerColor and gameID.
     * @param playerColor The provided playerColor.
     * @param gameID The provided gameID.
     */
    public JoinRequest(ChessGame.TeamColor playerColor, int gameID, String strAuthToken){
        this.playerColor = playerColor;
        this.gameID = gameID;
        this.strAuthToken = strAuthToken;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}