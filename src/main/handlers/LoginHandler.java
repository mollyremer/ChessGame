package handlers;

import com.google.gson.Gson;
import requests.LoginRequest;
import results.LoginResult;
import services.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

import static dataAccess.TreeDatabase.dbUsers;

public class LoginHandler extends HandlerParent implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        LoginRequest myRequest;
        LoginService service = new LoginService();

        try {
            myRequest = gson.fromJson(req.body(), LoginRequest.class);
        } catch (Exception e) {
            LoginResult myResult = new LoginResult(null, null, "Error: bad request");
            return jsonResult(res, myResult);
        }

        LoginResult myResult = service.login(myRequest);

        return jsonResult(res, myResult);
    }
}
