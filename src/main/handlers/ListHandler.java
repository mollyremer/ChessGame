package handlers;

import com.google.gson.Gson;
import requests.DefaultRequest;
import results.ListResult;
import services.ListService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class ListHandler implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
        Gson gson = new Gson();
        String strAuthToken = null;

        try {
            strAuthToken = req.headers("Authorization");
            System.out.println(strAuthToken);
        } catch (Exception e){
            res.status(401);
            ListResult result = new ListResult(null, "Error: unauthorized");
            return gson.toJson(result);
        }

        DefaultRequest request = new DefaultRequest(strAuthToken);
        ListService service = new ListService();
        ListResult result = service.list(request);

        res.status(200);
        if (Objects.equals(result.getMessage(), "Error: unauthorized")){
            res.status(401);
        } else if (result.getMessage() != null) {
            res.status(500);
        }

        return gson.toJson(result);
    }
}
