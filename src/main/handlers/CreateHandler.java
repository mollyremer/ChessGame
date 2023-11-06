package handlers;

import dataAccess.AuthDAO;
import requests.CreateRequest;
import results.DefaultResult;
import services.CreateService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateHandler extends HandlerParent implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        CreateRequest myRequest;
        DefaultResult myResult = new DefaultResult();
        CreateService service = new CreateService();

        try {
            myRequest = gson.fromJson(req.body(), CreateRequest.class);
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

        if((new AuthDAO()).findAuthToken(authToken) == null){
            myResult.setMessage("Error: unauthorized");
            return jsonResult(res, myResult);
        }

        myResult = service.create(myRequest);

        return jsonResult(res, myResult);
    }
}
