package results;

import models.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ListResult extends DefaultResult{
    /**
     * games A collection of ListEntryResult objects
     */
//    public Collection<GameInformation> games;
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
    }

//    public String getGameNames(){
//        StringBuilder gameNames = new StringBuilder();
//
//        for (GameInformation game : games){
//            if (game.getGameName() != null){
//                gameNames.append(game.getGameName());
//                if (game.whiteUsername != null){
//                    gameNames.append(" (White player is ");
//                    gameNames.append(game.whiteUsername);
//                    gameNames.append(")");
//                }
//                if (game.blackUsername != null){
//                    gameNames.append(" (Black player is ");
//                    gameNames.append(game.whiteUsername);
//                    gameNames.append(")");
//                }
//                gameNames.append(",");
//            }
//        }
//
//        return gameNames.toString();
//    }

}