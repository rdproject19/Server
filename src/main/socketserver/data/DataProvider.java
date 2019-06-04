package socketserver.data;

import org.java_websocket.WebSocket;
import socketserver.exceptions.ConversationNotFoundException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.protocol.Message;
import socketserver.util.LSFR;

import java.util.HashMap;
import java.util.Map;

public class DataProvider {

    DatabaseAdapter db;

    Map<String, UserConnection> users;
    Map<String, Conversation> conversations;

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

    public UserConnection getUser(String uid) throws UserNotFoundException {
        if (users.containsKey(uid)) {
            return users.get(uid);
        } else {
            db.userExists(uid);
        }
        return null;
    }

    public Conversation getConversation(String convID) throws ConversationNotFoundException {
        if (conversations.containsKey(convID)) {
            return conversations.get(convID);
        } else {
            return db.getConversation(convID);
        }
    }

    public void enqueueMessage(String recipient, Message message) {
        //No need to check if users exists, that was already verified
        db.queueMessage(recipient, message);
    }
}
