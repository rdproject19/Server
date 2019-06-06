package socketserver.data;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import socketserver.exceptions.ConversationNotFoundException;
import socketserver.exceptions.QueueObjectNotFoundException;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.protocol.Message;
import socketserver.server.MessageFactory;
import socketserver.util.LSFR;

import java.time.Instant;
import java.util.*;

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
    public void resetDBToken(String id, long count) {db.resetLSFR(id, count);}

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

    public void removeUser(WebSocket socket) {
        String found = "";
        for (String id : users.keySet()) {
            UserConnection c = users.get(id);
            if (c.getConnection().hashCode() == socket.hashCode()) {
                found = id;
            }
        }
        if (!found.equals(""))
            users.remove(found);
    }

    public Conversation getConversation(String convID) throws ConversationNotFoundException {
        if (conversations.containsKey(convID)) {
            return conversations.get(convID);
        } else {
            return db.getConversation(convID);
        }
    }

    public void enqueueMessage(String[] recipient, Message message) {
        if (recipient.length == 0) return;

        //No need to check if users exists, that was already verified
        UserQueueObject queueObject = new UserQueueObject("message", recipient.length, message);
        db.queueMessage(recipient, queueObject);
    }

    public String createUpdate(String userid) throws UnknownMessageTypeException {
        List<UserQueueObject> toAdd = null;
        try {
            toAdd = db.getQueue(userid);
        } catch (QueueObjectNotFoundException e) {
            e.printStackTrace();
            return new MessageFactory()
                    .setType("error")
                    .setStatusCode(404)
                    .setMessageString(e.toString())
                    .getBody();
        }
        if (toAdd == null) {
            toAdd = new ArrayList<>();
        }

        List<Conversation> conversations = new ArrayList<>();
        List<Message> messages = new ArrayList<>();

        for (UserQueueObject q : toAdd) {
            if (q.getType().equals("message")) {
                Message m = (Message) q.getData();
                //If the message is the delayed
                if (m.DELAYED) {
                    //If we're currently at or past the sending time, send the message
                    if (Instant.now().getEpochSecond() >= m.SEND_AT) {
                        messages.add(m);
                    }
                } else {
                    messages.add(m);
                }
            } else if (q.getType().equals("conversation")) {
                conversations.add((Conversation) q.getData());
            }
        }

        String update = "";
        update = new MessageFactory().setType("update")
                    .setNewConversations(conversations)
                    .setNewMessages(messages)
                    .getBody();

        return update;
    }
}
