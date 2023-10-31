package requests;

public class CreateRequest extends DefaultRequest{
    String gameName;

    /**
     * Constructs a new CreateRequest object with the provided gameName.
     * @param gameName The provided gameName.
     */
    public CreateRequest(String gameName, String strAuthToken){
        this.gameName = gameName;
        this.strAuthToken = strAuthToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
