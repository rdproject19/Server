package db;

import org.java_websocket.WebSocketImpl;
import util.LSFR;

public class UserCacheObject extends CacheObject {
    private String uid;
    private LSFR lsr;
    private WebSocketImpl connection;
    private int state = 0;

    public UserCacheObject(String u, LSFR l, WebSocketImpl c) {
        this.uid = u;
        this.lsr = l;
        this.connection = c;
    }

    public void shiftLSFR() {
        state = this.lsr.shift();
    }

    public int getToken() {
        return this.state;
    }
}
