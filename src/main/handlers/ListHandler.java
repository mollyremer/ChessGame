package handlers;

import requests.DefaultRequest;
import results.ListResult;
import services.ListService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListHandler extends HandlerParent implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
        DefaultRequest request = new DefaultRequest();
        ListService service = new ListService();

        try {
            authToken = req.headers("Authorization");
        } catch (Exception e){
            ListResult myResult = new ListResult(null, "Error: unauthorized");
            return jsonResult(res, myResult);
        }

        request.setStrAuthToken(authToken);

        ListResult result = service.list(request);

        return jsonResult(res, result);
    }
}
