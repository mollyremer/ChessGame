package handlers;

import com.google.gson.Gson;
import results.DefaultResult;
import services.ClearService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        System.out.println("handling clear request....");
        Gson gson = new Gson();

        ClearService service = new ClearService();
        DefaultResult result = service.clear();


        res.status(200);
        if (result.getMessage() != null){
            res.status(500);
        }

        return gson.toJson(result);
    }

}
