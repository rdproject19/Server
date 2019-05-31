package data;

import org.java_websocket.WebSocket;

public class UserConnection {
    private WebSocket connection;
    private boolean authenticated;

    public UserConnection(WebSocket socket) {
        this.connection = socket;
    }

    public void setAuthenticated() {
        this.authenticated = true;
    }

    public WebSocket getConnection() {
        return connection;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
