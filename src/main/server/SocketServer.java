package server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class SocketServer extends WebSocketServer {

<<<<<<< HEAD
    private MessageHandler messageHandler;

    public SocketServer(InetSocketAddress addr) {
        super(addr);
        this.messageHandler = new MessageHandler(this);
=======
    public SocketServer(InetSocketAddress addr) {
        super(addr);
>>>>>>> fca15036019b012fd153030cfe8b1230138f3971
    }

    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    public void onMessage(WebSocket webSocket, String s) {
        //Echo for test purposes
        webSocket.send("Received: " + s);
<<<<<<< HEAD
        messageHandler.receiveMessage(s);
=======
>>>>>>> fca15036019b012fd153030cfe8b1230138f3971
    }

    public void onError(WebSocket webSocket, Exception e) {

    }

    public void onStart() {
        System.out.println("Server started");
    }

}
