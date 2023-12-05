package handlers;

import com.google.gson.Gson;
import results.DefaultResult;
import spark.Response;

import java.util.Objects;

public class HandlerParent {
    protected Gson gson = new Gson();
    protected String authToken = null;

    public void setResStatus(Response res, DefaultResult myResult){
        res.status(200);

        if (Objects.equals(myResult.getMessage(), "Error: bad request")){
            res.status(400);
        } else if (Objects.equals(myResult.getMessage(), "Error: unauthorized")) {
            res.status(401);
        } else if (Objects.equals(myResult.getMessage(), "Error: already taken")) {
            res.status(403);
        } else if (myResult.getMessage() != null) {
            res.status(500);
        }
    }

    public Object jsonResult(Response res, DefaultResult result){
        try {
            setResStatus(res, result);
            return gson.toJson(result);
        } catch (Exception e){
            System.out.println("Here's the error at least");
        }
        return null;
    }

}
