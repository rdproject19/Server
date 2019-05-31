package data;

import exceptions.UserNotFoundException;
import org.java_websocket.WebSocket;
import util.LSFR;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProvider {

    DatabaseAdapter db;

    Map<String, UserConnection> users;

    public DataProvider(DatabaseAdapter databaseAdapter) {
        this.db = databaseAdapter;
        users = new HashMap<>();
    }

    public int getNewToken(String uid) throws UserNotFoundException {
        LSFR l = db.getUserLSFR(uid);
        return l.shift();
    }

    public void addUser(String uid, WebSocket conn) {
        UserConnection uconn = new UserConnection(conn);
        uconn.setAuthenticated();

        users.put(uid, uconn);
    }
}
