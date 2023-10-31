package results;

public class CreateResult {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    private int gameID;

    public CreateResult(int gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }
}
