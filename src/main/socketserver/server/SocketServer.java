package socketserver.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import socketserver.data.DataProvider;
import socketserver.data.DatabaseAdapter;
import socketserver.util.Configuration;

import java.net.InetSocketAddress;

public class SocketServer extends WebSocketServer {

    private socketserver.server.MessageHandler messageHandler;
    private DataProvider dataProvider;

    public SocketServer(InetSocketAddress addr, Configuration c) {
        super(addr);
        this.dataProvider = new DataProvider(new DatabaseAdapter(c.getDatabaseHost(), c.getDatabasePort()));
        this.messageHandler = new socketserver.server.MessageHandler(this, dataProvider);
    }

    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Connected new client");
    }

    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    public void onMessage(WebSocket webSocket, String s) {
        //Echo for test purposes
        webSocket.send("Received: " + s);
        messageHandler.receiveMessage(s, webSocket);
    }

    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    public void onStart() {
        System.out.println("Server started");

    }

}
