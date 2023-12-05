package results;

public class DefaultResult {
    public String message;
    public DefaultResult(){}

    public DefaultResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
