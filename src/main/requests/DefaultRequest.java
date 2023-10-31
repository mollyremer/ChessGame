package requests;

public class DefaultRequest {
    public String getStrAuthToken() {
        return strAuthToken;
    }

    public void setStrAuthToken(String strAuthToken) {
        this.strAuthToken = strAuthToken;
    }

    public DefaultRequest(String strAuthToken) {
        this.strAuthToken = strAuthToken;
    }

    String strAuthToken;
}
