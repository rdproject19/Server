package socketserver.data;

import org.java_websocket.WebSocket;
import socketserver.exceptions.ConversationNotFoundException;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.protocol.Message;
import socketserver.server.MessageFactory;
import socketserver.util.LSFR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProvider {

    DatabaseAdapter db;

    Map<String, UserConnection> users;
    Map<String, Conversation> conversations;

    public DataProvider(DatabaseAdapter databaseAdapter) {
        this.db = databaseAdapter;
        users = new HashMap<>();
        conversations = new HashMap<>();
    }

    public LSFR getLSFR(String uid) throws UserNotFoundException {
        return db.getUserLSFR(uid);
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
        UserQueueObject<Message> queueObject = new UserQueueObject<>("message", message);
        db.queueMessage(recipient, queueObject);
    }

    public String createUpdate(String userid) {
        List<UserQueueObject> toAdd = db.getQueue(userid);
        if (toAdd == null) {
            toAdd = new ArrayList<>();
        }

        List<Conversation> conversations = new ArrayList<>();
        List<Message> messages = new ArrayList<>();

        for (UserQueueObject q : toAdd) {
            if (q.getType().equals("message")) {
                messages.add((Message) q.getData());
            } else if (q.getType().equals("conversation")) {
                conversations.add((Conversation) q.getData());
            }
        }

        String update = "";
        try {
            update = new MessageFactory().setType("update")
                    .setNewConversations(conversations)
                    .setNewMessages(messages)
                    .getBody();
        } catch (UnknownMessageTypeException e) {
            e.printStackTrace();
        }

        return update;
    }
}
