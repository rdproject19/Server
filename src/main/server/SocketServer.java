package server;

import com.google.common.hash.Hashing;
import data.DataProvider;
import data.UserCacheObject;
import exceptions.UserNotFoundException;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class SocketServer extends WebSocketServer {

    private MessageHandler messageHandler;
    private DataProvider dataProvider;

    public SocketServer(InetSocketAddress addr) {
        super(addr);
        this.dataProvider = new DataProvider();
        this.messageHandler = new MessageHandler(this, dataProvider);
    }

    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        dataProvider.cache.addUser("koen", Hashing.sha512()
                .hashString("gewgwegwwgegwghwewegwwherhjerhjer", StandardCharsets.UTF_8)
                .toString(), webSocket);
        Set<UserCacheObject> testconvo = new HashSet<>();
        try {
            testconvo.add(dataProvider.cache.getUser("koen"));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        this.dataProvider.cache.addConversation("test", testconvo);
    }

    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    public void onMessage(WebSocket webSocket, String s) {
        //Echo for test purposes
        webSocket.send("Received: " + s);
        messageHandler.receiveMessage(s);
    }

    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    public void onStart() {
        System.out.println("Server started");

    }

}
