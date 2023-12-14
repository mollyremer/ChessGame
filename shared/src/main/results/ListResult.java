package results;

import models.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ListResult extends DefaultResult{
    /**
     * games A collection of ListEntryResult objects
     */

    public ArrayList<GameInformation> games;

    public ListResult(ArrayList<GameInformation> games, String message) {
        this.games = games;
        this.message = message;
    }

    public GameInformation getGame(String gameName){
        for (GameInformation gameInformation : games){
            if (Objects.equals(gameInformation.getGameName(), gameName)){
                return gameInformation;
            }
        }

        return null;
    }
    public static class GameInformation {
        public int gameID;
        public String whiteUsername;
        public String blackUsername;
        public String gameName;

        public GameInformation(int gameID, String whiteUsername, String blackUsername, String gameName) {
            this.gameID = gameID;
            this.whiteUsername = whiteUsername;
            this.blackUsername = blackUsername;
            this.gameName = gameName;
        }

        public String getGameName(){
            return gameName;
        }
        public int getGameID() {return gameID;}
    }

}