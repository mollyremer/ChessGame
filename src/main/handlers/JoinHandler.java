package handlers;

import requests.JoinRequest;
import results.DefaultResult;
import services.JoinService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinHandler extends HandlerParent implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        JoinRequest myRequest;
        DefaultResult myResult = new DefaultResult();
        JoinService service = new JoinService();

        try {
            myRequest = gson.fromJson(req.body(), JoinRequest.class);
        } catch (Exception e) {
            myResult.setMessage("Error: bad request");
            return jsonResult(res, myResult);
        }

        try {
            authToken = req.headers("Authorization");
        } catch (Exception e){
            myResult.setMessage("Error: unauthorized");
            return jsonResult(res, myResult);
        }

        myRequest.setStrAuthToken(authToken);
        myResult = service.join(myRequest);

        return jsonResult(res, myResult);
    }
}





