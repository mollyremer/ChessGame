package results;

public class RegisterResult extends DefaultResult{
    /**
     * username The provided username
     */
    String username;
    String authToken;

    public RegisterResult(String username, String authToken, String message){
        this.username = username;
        this.authToken = authToken;
        this.message = message;
    }

    public RegisterResult(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
