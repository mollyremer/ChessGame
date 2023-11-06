package handlers;

import results.DefaultResult;
import services.ClearService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler extends HandlerParent implements Route {
    @Override
    public Object handle(Request req, Response res) throws Exception {
        ClearService service = new ClearService();
        DefaultResult myResult = service.clear();

        return jsonResult(res, myResult);
    }

}
