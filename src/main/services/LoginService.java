package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import requests.LoginRequest;
import results.LoginResult;
import results.RegisterResult;

import java.util.Objects;
import java.util.UUID;

public class LoginService {
    /**
     * Logs in an existing user (returns a new authToken)
     * @param request The request to clear the database.
     * @return The response from the server.
     */
    public LoginResult login(LoginRequest request) throws DataAccessException {
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();

        String username, password;
        try {
            username = request.getUsername();
            password = request.getPassword();
        } catch (Exception e) {
            return new LoginResult(null, null, "Error: bad request");
        }

        if (username == null || password == null || username.isEmpty() || password.isEmpty()){
            return new LoginResult(null, null, "Error: bad request");
        }

        if (userDAO.findUserByUsername(username) == null) {
            return new LoginResult(null, null, "Error: unauthorized");
        }

        if (!Objects.equals(userDAO.findUserByUsername(username).getPassword(), password)){
            return new LoginResult(null,null, "Error: unauthorized" );
        }

        String strAuthToken = UUID.randomUUID().toString();
        AuthToken objAuthToken = new AuthToken(strAuthToken, username);
        authDAO.insertAuthToken(objAuthToken);

        return new LoginResult(strAuthToken, username, null);
    }
}
