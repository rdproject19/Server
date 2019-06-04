package socketserver.data;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import socketserver.exceptions.ConversationNotFoundException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.util.LSFR;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class DatabaseAdapter {

    private static final String USER_DB_NAME = "users";
    private static final String CONVERSATION_DB_NAME = "conversations";

    //Actual connection
    private final MongoClient connection;

    //Databases
    private MongoDatabase users = null;
    private MongoDatabase conversations = null;

    public DatabaseAdapter(String host, int port) {
        connection = MongoClients.create("mongodb://" + host + ":" + port);
        if (!initDatabases()) {
            System.out.println("FATAL - One of the required databases does not exist! Please check configuration.");
            System.exit(-2);
        }
    }

    public LSFR getUserLSFR(String uid) throws UserNotFoundException {
        FindIterable it = users.getCollection("usercollection").find(eq("username", uid));
        Document user = (Document) it.first();
        if (user != null) {
            if (user.containsKey("state")) {
                String stateString = user.getString("state");
                byte[] b = stateString.getBytes();
                long shiftcount = user.getLong("shiftcount");

                return new LSFR(b, shiftcount);
            } else {
                String token = user.getString("token");
                return new LSFR(token);
            }
        } else {
            throw new UserNotFoundException(uid);
        }
    }

    public void updateLSFR(String id, LSFR l) {
        FindIterable user = users.getCollection("usercollection").find(eq("username", id));
        if (user.first() != null) {
            users.getCollection("usercollection")
                    .updateOne(eq("username", id),
                    combine(
                            set("state", l.getStateString()),
                            set("shiftcount", l.getShiftCount())
                    ));
        }
    }

    public void userExists(String uid) throws UserNotFoundException {
        FindIterable user = users.getCollection("usercollection").find(eq("username", uid));
        if (user.first() == null) throw new UserNotFoundException(uid);
    }

    public Conversation getConversation(String gid) throws ConversationNotFoundException {
        FindIterable conv = conversations.getCollection("conversationcollection").find(eq("_id", new ObjectId(gid)));
        if (conv.first() == null) {
            throw new ConversationNotFoundException(gid);
        } else {
            return Conversation.fromDocument((Document) conv.first());
        }
    }

    public void queueMessage(String id, UserQueueObject data) {
        users.getCollection("usercollection").updateOne(eq("username", id), addToSet("queue", data));
    }

    public List<UserQueueObject> getQueue(String id) {
        return users.getCollection("usercollection").find(eq("username", id)).first().getList("queue", UserQueueObject.class);
    }

    private boolean initDatabases() {
        try {
            users = connection.getDatabase(USER_DB_NAME);
            conversations = connection.getDatabase(CONVERSATION_DB_NAME);
            return true;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
