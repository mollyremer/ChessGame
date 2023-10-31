package results;

import models.AuthToken;
import requests.RegisterRequest;
import spark.Response;

public class RegisterResult{
    /**
     * username The provided username
     */
    String username;
    String authToken;
    String message;

    public RegisterResult(String username, String authToken, String message){
        this.username = username;
        this.authToken = authToken;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
