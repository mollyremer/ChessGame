package services;

import dataAccess.AuthDAO;
import models.AuthToken;
import requests.DefaultRequest;
import results.DefaultResult;

public class LogoutService {
    /**
     * Logs out the user represented by the authToken.
     * @param request The request to clear the database.
     * @return The response from the server.
     */
    public DefaultResult logout(DefaultRequest request){
        try{
            AuthDAO authDAO = new AuthDAO();
            AuthToken objAuthToken = authDAO.findAuthToken(request.getStrAuthToken());
            authDAO.removeAuthToken(objAuthToken);
        } catch (Exception e){
            return new DefaultResult("Error: unauthorized");
        }

        return new DefaultResult(null);
    }
}
