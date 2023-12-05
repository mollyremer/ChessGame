package requests;

public class LoginRequest extends DefaultRequest{
    private String username;
    private final String password;

    /**
     * Constructs a new LoginRequest object with the provided username and password.
     * @param username The username associated with the login request.
     * @param password The password associated with the login request.
     */
    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String username) {
        this.username = username;
    }
}
