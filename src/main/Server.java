import handlers.*;
import spark.Spark;

public class Server {
    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        Spark.port(8080);

        Spark.externalStaticFileLocation("web");

        Spark.delete("/db", new ClearHandler());
        Spark.post("/user", new RegisterHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
        Spark.post("/game", new CreateHandler());
        Spark.put("/game", new JoinHandler());
        Spark.get("/game", new ListHandler());
    }
}
