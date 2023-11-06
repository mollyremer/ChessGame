package requests;

public class DefaultRequest {
    String strAuthToken = null;
    public DefaultRequest(){}
    public DefaultRequest(String strAuthToken) {
        this.strAuthToken = strAuthToken;
    }

    public String getStrAuthToken() {
        return strAuthToken;
    }

    public void setStrAuthToken(String strAuthToken) {
        this.strAuthToken = strAuthToken;
    }

}
