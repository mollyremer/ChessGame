package client;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.LeaveCommand;
import webSocketMessages.userCommands.ResignCommand;

public class WebSocketCommunicator {
    private final String serverURL;
    private Client ws = new Client();

    public WebSocketCommunicator(String serverURL){ this.serverURL = serverURL; }

    public void sendCommand(Object command) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(command);
            ws.send(json);
        } catch (Exception e) {
            System.out.println("Error in parsing the message");
            throw new RuntimeException(e);
        }
    }
}
