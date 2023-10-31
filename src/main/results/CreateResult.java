package results;

public class CreateResult extends DefaultResult{
    private int gameID;

    public CreateResult(int gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
