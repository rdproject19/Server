package socketserver.data;

import org.java_websocket.WebSocket;

public class UserConnection {
    private WebSocket connection;
    private boolean authenticated;

    private boolean active;

    public UserConnection(WebSocket socket) {
        this.connection = socket;
        active = true;
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

    public boolean isActive() {return active;}
    public void setActive(boolean val) {this.active = val;}
}
