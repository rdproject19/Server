package server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class SocketServer extends WebSocketServer {

    public SocketServer(InetSocketAddress addr) {
        super(addr);
    }

    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    public void onMessage(WebSocket webSocket, String s) {
        //Echo for test purposes
        webSocket.send("Received: " + s);
    }

    public void onError(WebSocket webSocket, Exception e) {

    }

    public void onStart() {
        System.out.println("Server started");
    }

}
