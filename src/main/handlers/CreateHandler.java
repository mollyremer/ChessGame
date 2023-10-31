package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import requests.CreateRequest;
import results.CreateResult;
import results.DefaultResult;
import services.CreateService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateHandler implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        System.out.println("handling create request....");
        System.out.println("Request Body: " + req.body());

        Gson gson = new Gson();
        CreateRequest request;

        try {
            request = gson.fromJson(req.body(), CreateRequest.class);
        } catch (Exception e) {
            DefaultResult result = new DefaultResult("Error: bad request");
            res.status(400);
            return gson.toJson(result);
        }

        String strAuthToken = null;

        try {
            strAuthToken = req.headers("Authorization");
            System.out.println(strAuthToken);
        } catch (Exception e){
            res.status(401);
            DefaultResult result = new DefaultResult("Error: unauthorized");
            return gson.toJson(result);
        }

        if((new AuthDAO()).findAuthToken(strAuthToken) == null){
            res.status(401);
            DefaultResult result = new DefaultResult("Error: unauthorized");
            return gson.toJson(result);
        }

        CreateService service = new CreateService();
        CreateResult result = service.create(request);

        if (result.getMessage() != null){
            res.status(500);
        }

        return gson.toJson(result);
    }
}
