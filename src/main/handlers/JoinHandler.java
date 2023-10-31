package handlers;

import com.google.gson.Gson;
import requests.JoinRequest;
import results.DefaultResult;
import services.JoinService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class JoinHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
        System.out.println("handling join request....");
        System.out.println("Request Body: " + req.body());

        Gson gson = new Gson();
        JoinRequest request;

        try {
            request = gson.fromJson(req.body(), JoinRequest.class);
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
        request.setStrAuthToken(strAuthToken);

        JoinService service = new JoinService();

        DefaultResult result = service.join(request);

        res.status(200);
        if (Objects.equals(result.getMessage(), "Error: bad request")){
            res.status(400);
        } else if (Objects.equals(result.getMessage(), "Error: already taken")) {
            res.status(403);
        } else if (Objects.equals(result.getMessage(), "Error: unauthorized")){
            res.status(401);
        }
        else if (result.getMessage() != null) {
            res.status(500);
        }


        return gson.toJson(result);
    }
}
