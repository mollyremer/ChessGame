package handlers;
import com.google.gson.Gson;
import requests.DefaultRequest;
import results.DefaultResult;
import services.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        String strAuthToken = null;
        Gson gson = new Gson();

        try {
            strAuthToken = req.headers("Authorization");
            System.out.println(strAuthToken);
        } catch (Exception e){
            res.status(401);
            return new DefaultResult("Error: unauthorized");
        }

        DefaultRequest request = new DefaultRequest(strAuthToken);

        LogoutService service = new LogoutService();

        DefaultResult result = service.logout(request);

        res.status(200);
        if (Objects.equals(result.getMessage(), "Error: unauthorized")){
            res.status(401);
        } else if (result.getMessage() != null){
            res.status(500);
        }

        return gson.toJson(result);
    }
}
