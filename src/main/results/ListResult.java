package results;

import java.lang.reflect.Array;
import java.util.Collection;

public class ListResult {
    /**
     * games A collection of ListEntryResult objects
     */
    Object[] games;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    public ListResult(Object[] games, String message) {
        this.games = games;
        this.message = message;
    }

    public static class ListEntryResult {
        public int gameID;
        public String whiteUsername;
        public String blackUsername;
        public String gameName;

        public ListEntryResult(int gameID, String whiteUsername, String blackUsername, String gameName) {
            this.gameID = gameID;
            this.whiteUsername = whiteUsername;
            this.blackUsername = blackUsername;
            this.gameName = gameName;
        }
    }

}