package server;

import com.google.common.hash.Hashing;
import data.DataProvider;
import data.DatabaseAdapter;
import data.UserConnection;
import exceptions.UserNotFoundException;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import util.Configuration;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class SocketServer extends WebSocketServer {

    private MessageHandler messageHandler;
    private DataProvider dataProvider;

    public SocketServer(InetSocketAddress addr, Configuration c) {
        super(addr);
        this.dataProvider = new DataProvider(new DatabaseAdapter(c.getDatabaseHost(), c.getDatabasePort()));
        this.messageHandler = new MessageHandler(this, dataProvider);
    }

    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    public void onMessage(WebSocket webSocket, String s) {
        //Echo for test purposes
        //webSocket.send("Received: " + s);
        messageHandler.receiveMessage(s, webSocket);
    }

    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    public void onStart() {
        System.out.println("Server started");

    }

}
