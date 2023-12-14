package webSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public Session session;
    public String clientAuthToken;

    public Connection(String clientAuthToken, Session session) {
        this.clientAuthToken = clientAuthToken;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getClientAuthToken() {
        return clientAuthToken;
    }

    public void setClientAuthToken(String clientAuthToken) {
        this.clientAuthToken = clientAuthToken;
    }
}
