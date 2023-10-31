package requests;

public class CreateRequest {
    String gameName;

    /**
     * Constructs a new CreateRequest object with the provided gameName.
     * @param gameName The provided gameName.
     */
    public CreateRequest(String gameName){
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
