package requests;

public class DefaultRequest {
    String authToken = null;
    public DefaultRequest(){}
    public DefaultRequest(String strAuthToken) {
        this.authToken = strAuthToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
