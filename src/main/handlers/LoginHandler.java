package handlers;

import com.google.gson.Gson;
import requests.LoginRequest;
import results.LoginResult;
import services.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

import static dataAccess.Database.dbUsers;

public class LoginHandler implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        System.out.println("handling login request....");
        System.out.println("Request Body: " + req.body());

        Gson gson = new Gson();
        LoginRequest request;

        try {
            request = gson.fromJson(req.body(), LoginRequest.class);
        } catch (Exception e) {
            LoginResult result = new LoginResult(null, null, "Error: bad request");
            res.status(400);
            return gson.toJson(result);
        }

        LoginService service = new LoginService();

        LoginResult result = service.login(request);

        res.status(200);
        if (Objects.equals(result.getMessage(), "Error: unauthorized")){
            res.status(401);
        } else if (result.getMessage() != null) {
            res.status(500);
        }

        return gson.toJson(result);
    }
}
