package handlers;

import com.google.gson.Gson;
import requests.RegisterRequest;
import results.RegisterResult;
import services.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class RegisterHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
        System.out.println("handling register request....");
        System.out.println("Request Body: " + req.body());

        Gson gson = new Gson();
        RegisterRequest request;

        try {
            request = gson.fromJson(req.body(), RegisterRequest.class);
        } catch (Exception e) {
            RegisterResult result = new RegisterResult(null, null, "Error: bad request");
            res.status(400);
            return gson.toJson(result);
        }

        RegisterService service = new RegisterService();

        RegisterResult result = service.register(request);

        res.status(200);
        if (Objects.equals(result.getMessage(), "Error: bad request")) {
            res.status(400);
        } else if (Objects.equals(result.getMessage(), "Error: already taken")) {
            res.status(403);
        } else if (result.getMessage() != null) {
            res.status(500);
        }

        return gson.toJson(result);
    }
}
