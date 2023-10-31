package results;
public class DefaultResult {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public DefaultResult(String message) {
        this.message = message;
    }
}
