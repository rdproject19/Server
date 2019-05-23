package protocol;

import data.DataProvider;
import org.java_websocket.WebSocket;

public class Handshake extends BaseMessage {

    public String uid;

    public Handshake(String type) {
        super(type);
    }

    public void handle(DataProvider dp, WebSocket conn) {
        dp.cache.addUser(uid, conn);
    }
}
