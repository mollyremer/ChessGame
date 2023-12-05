package requests;

public class RegisterRequest extends DefaultRequest{
    String username;
    String password;
    String email;

    /**
     * Constructs a new RegisterRequest object with the provided username, password, and email.
     * @param username The provided username.
     * @param password The provided password.
     * @param email The provided email.
     */
    public RegisterRequest(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
