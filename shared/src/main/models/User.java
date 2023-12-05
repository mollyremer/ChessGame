package models;

import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }

    /**
     * Creates a new user object.
     * @param username A unique user-made string
     * @param password A unique user-made string
     * @param email An email for the user
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String s){
        username = s;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String s){
        password = s;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String s){
        email = s;
    }
}
