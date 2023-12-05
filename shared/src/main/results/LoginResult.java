package results;

public class LoginResult extends DefaultResult{
    private String authToken;
    private String username;

    /**
     * Constructs a LoginResult for the provided message, authToken, username.
     * @param message The provided message.
     * @param authToken The provided authToken.
     * @param username The provided username.
     */

    public LoginResult(String authToken, String username, String message){
        this.message = message;
        this.authToken = authToken;
        this.username = username;
    }

    public LoginResult(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
