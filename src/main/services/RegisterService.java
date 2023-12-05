package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;
import requests.RegisterRequest;
import results.RegisterResult;

import java.util.Objects;
import java.util.UUID;

public class RegisterService {
    /**
     * Register a new user.
     * @param request The request to register a new user.
     * @return The response from the server.
     */
    public RegisterResult register(RegisterRequest request) throws DataAccessException {

        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();

        String username, password, email;
        try {
            username = request.getUsername();
            password = request.getPassword();
            email = request.getEmail();
        } catch (Exception e) {
            return new RegisterResult(null, null, "Error: bad request");
        }

        if (username == null || password == null || email == null || username.isEmpty() || password.isEmpty() || email.isEmpty()){
            return new RegisterResult(null, null, "Error: bad request");
        }

        User user = new User(username, password, email);

        if (userDAO.findUserByUsername(user.getUsername()) != null) {
            return new RegisterResult(null, null, "Error: already taken");
        }

        try {
            userDAO.insertUser(user);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: already taken")){
                return new RegisterResult(null, null, "Error: already taken");
            }
            else{
                return new RegisterResult(null, null, e.getMessage());
            }
        }


        String strAuthToken = UUID.randomUUID().toString();
        AuthToken objAuthToken = new AuthToken(strAuthToken, username);
        authDAO.insertAuthToken(objAuthToken);

        return new RegisterResult(username, strAuthToken, null);

    }
}
