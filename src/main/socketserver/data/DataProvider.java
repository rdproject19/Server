package socketserver.data;

import org.java_websocket.WebSocket;
import socketserver.exceptions.UserNotFoundException;
import socketserver.util.LSFR;

import java.util.HashMap;
import java.util.Map;

public class DataProvider {

    DatabaseAdapter db;

    Map<String, UserConnection> users;

    public DataProvider(DatabaseAdapter databaseAdapter) {
        this.db = databaseAdapter;
        users = new HashMap<>();
    }

    public LSFR getLSFR(String uid) throws UserNotFoundException {
        LSFR l = db.getUserLSFR(uid);
        return l;
    }

    public void shiftDBToken(String id, LSFR l) {
        db.updateLSFR(id, l);
    }

    public void addUser(String uid, WebSocket conn) {
        UserConnection uconn = new UserConnection(conn);
        uconn.setAuthenticated();

        users.put(uid, uconn);
    }
}
