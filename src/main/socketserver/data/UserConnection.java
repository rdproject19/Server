package socketserver.data;

import org.java_websocket.WebSocket;

/**
 * Basic object representing a user connected to the websocket server
 */
public class UserConnection {
    private WebSocket connection;
    private boolean authenticated;

    public UserConnection(WebSocket socket) {
        this.connection = socket;
    }

    public void setAuthenticated() {
        this.authenticated = true;
    }

    public int getConnectionCode() {
        return connection.hashCode();
    }

    /**
     * Send a message to the user, if they have been authenticated
     * @param message The message to send
     */
    public void sendMessage(String message) {
        if (authenticated) {
            connection.send(message);
        }
    }
}
