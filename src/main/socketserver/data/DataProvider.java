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

/**
 * Handles all data management within the server
 */
public class DataProvider {

    //DB
    DatabaseAdapter db;

    //Cache
    Map<String, UserConnection> users;
    Map<String, Conversation> conversations;
    private static final int MAX_CONVERSATION_SIZE = 1000; //How many conversation may be cached

    /**
     * Creates a new DataProvider
     * @param databaseAdapter The databaseadapter to use
     */
    public DataProvider(DatabaseAdapter databaseAdapter) {
        this.db = databaseAdapter;
        users = new HashMap<>();
        conversations = new HashMap<>();
    }

    /**
     * Gets an LSFR
     * @param uid The user for whom to retrieve the LSFR
     * @return The LSFR
     * @throws UserNotFoundException If the given user does not exist
     */
    public LSFR getLSFR(String uid) throws UserNotFoundException {
        return db.getUserLSFR(uid);
    }

    /**
     * Shifts the LSFR (updates)
     * @param id The user for whom to shift the LSFR
     * @param l The shifted LSFR
     */
    public void shiftDBToken(String id, LSFR l) {
        db.updateLSFR(id, l);
    }

    /**
     * Desync prcedure:
     * - Get the initial token from the database
     * - Shift this token by a random shiftcount
     * - Transform the current state of that LSFR to a token
     * - Store this token and reset the shiftcount to 0
     *
     * Resets the LSFR in the database (Desync procedure)
     * @param id The user id for whom to reset the LSFR
     * @param count The new shiftcount
     */
    public void resetDBToken(String id, long count) {db.resetLSFR(id, count);}

    /**
     * Adds a user to the user cache
     * @param uid User id of the user
     * @param conn Associated websocket connection
     */
    public void addUser(String uid, WebSocket conn) {
        UserConnection uconn = new UserConnection(conn);
        uconn.setAuthenticated();

        users.put(uid, uconn);
    }

    /**
     * Gets a user from the user cache
     * @param uid User id of the user to get
     * @return A UserConnection object corresponding to the user id
     * @throws UserNotFoundException If the given user id does not exist in the user cache
     */
    public UserConnection getUser(String uid) throws UserNotFoundException {
        if (users.containsKey(uid)) {
            return users.get(uid);
        } else {
            db.userExists(uid);
        }
        return null;
    }

    /**
     * Removes a user from the user cache (on disconnect)
     * @param socket The websocket of this user
     */
    public void removeUser(WebSocket socket) {
        String found = "";
        for (String id : users.keySet()) {
            UserConnection c = users.get(id);
            if (c.getConnectionCode() == socket.hashCode()) {
                found = id;
            }
        }
        if (!found.equals(""))
            users.remove(found);
    }

    /**
     * Gets a conversation from the cache if available, else from the database. If retrieved from the database, conversation will be cached, if there is room.
     * @param convID The conversation id
     * @return A conversation
     * @throws ConversationNotFoundException If the given conversation id does not exist
     */
    public Conversation getConversation(String convID) throws ConversationNotFoundException {
        if (conversations.containsKey(convID)) {
            Conversation c = conversations.get(convID);
            c.cacheObjectAccessed();
            conversations.replace(convID, c);
            return c;
        } else {
            Conversation c = db.getConversation(convID);
            if (conversations.size() < MAX_CONVERSATION_SIZE) {
                conversations.put(convID, c);
            }
            return c;
        }
    }

    /**
     * Enqueues a message
     * @param recipient List of recipient user ids
     * @param message The message to enqueue
     */
    public void enqueueMessage(String[] recipient, Message message) {
        if (recipient.length == 0) return;

        //No need to check if users exists, that was already verified
        UserQueueObject queueObject = new UserQueueObject("message", recipient.length, message);
        db.queueMessage(recipient, queueObject);
    }

    /**
     * Creates a json update
     * @param userid User id for whom to create the update
     * @return A json string containing the update
     */
    public String createUpdate(String userid) {
        List<UserQueueObject> toAdd = null;
        try {
            toAdd = db.getQueue(userid);
        } catch (QueueObjectNotFoundException e) {
            e.printStackTrace();
            try {
                return new MessageFactory()
                        .setType("error")
                        .setStatusCode(404)
                        .setMessageString(e.toString())
                        .getBody();
            } catch (UnknownMessageTypeException ex) {
                ex.printStackTrace();
            }
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
                    if ((Instant.now().getEpochSecond() * 1000) >= m.SEND_AT) {
                        messages.add(m);
                    }
                } else {
                    messages.add(m);
                }
            } else if (q.getType().equals("conversation")) {
                conversations.add((Conversation) q.getData());
            }
        }

        try {
            return new MessageFactory().setType("update")
                    .setNewConversations(conversations)
                    .setNewMessages(messages)
                    .getBody();
        } catch (UnknownMessageTypeException e) {
            e.printStackTrace();
        }
        return "";
    }
}
