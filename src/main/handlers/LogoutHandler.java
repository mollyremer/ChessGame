package handlers;
import com.google.gson.Gson;
import requests.DefaultRequest;
import results.DefaultResult;
import services.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class LogoutHandler extends HandlerParent implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        DefaultRequest myRequest;
        DefaultResult myResult = new DefaultResult();
        LogoutService service = new LogoutService();

        try {
            authToken = req.headers("Authorization");
        } catch (Exception e){
            myResult.setMessage("Error: unauthorized");
            return jsonResult(res, myResult);
        }

        myRequest = new DefaultRequest(authToken);
        myResult = service.logout(myRequest);

        setResStatus(res, myResult);

        return jsonResult(res, myResult);
    }
}
