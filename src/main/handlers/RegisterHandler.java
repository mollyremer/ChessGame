package handlers;

import requests.RegisterRequest;
import results.RegisterResult;
import services.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler extends HandlerParent implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
        RegisterRequest myRequest;
        RegisterResult myResult;
        RegisterService service = new RegisterService();

        try {
            myRequest = gson.fromJson(req.body(), RegisterRequest.class);
        } catch (Exception e) {
            myResult = new RegisterResult(null, null, "Error: bad request");
            return jsonResult(res, myResult);
        }

        myResult = service.register(myRequest);
        setResStatus(res, myResult);

        return jsonResult(res, myResult);
    }
}
