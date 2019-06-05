package socketserver.data;

import com.google.common.hash.Hashing;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import socketserver.exceptions.ConversationNotFoundException;
import socketserver.exceptions.QueueObjectNotFoundException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.util.LSFR;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
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

    public void resetLSFR(String id, long newcount) {
        FindIterable user = users.getCollection("usercollection").find(eq("username", id));
        if (user.first() != null) {
            Document d = (Document) user.first();
            LSFR l = new LSFR(d.getString("token"), newcount);

            String newseed = Hashing.sha512().hashString(l.getStateString(), StandardCharsets.UTF_8).toString();

            users.getCollection("usercollection")
                    .updateOne(eq("username", id),
                            combine(
                                    unset("state"),
                                    set("shiftcount", 0),
                                    set("token", newseed)
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

    public void queueMessage(String[] ids, UserQueueObject data) {
        Document d = data.toDocument();
        conversations.getCollection("queue").insertOne(d);

        ObjectId oid = d.getObjectId("_id");

        users.getCollection("usercollection").updateMany(in("username", ids), addToSet("queue", oid));
    }

    public List<UserQueueObject> getQueue(String id) throws QueueObjectNotFoundException {
        List<ObjectId> queued = users.getCollection("usercollection").find(eq("username", id)).first().getList("queue", ObjectId.class);
        if (queued == null) {
            return new ArrayList<>();
        }
        List<UserQueueObject> result = new ArrayList<>();

        for (ObjectId oid : queued) {
            Document d = conversations.getCollection("queue").find(eq("_id", oid)).first();
            if (d == null) {
                throw new QueueObjectNotFoundException(oid.toString());
            }
            UserQueueObject queueObject = UserQueueObject.fromDocument(d);
            result.add(queueObject);
            if (queueObject.receivedByAll()) {
                conversations.getCollection("queue").deleteOne(eq("_id", oid));
            } else {
                conversations.getCollection("queue").updateOne(eq("_id", oid), inc("received", 1));
            }
        }

        users.getCollection("usercollection").updateOne(eq("username", id), set("queue", new ArrayList<>()));

        return result;
    }

    public boolean documentExists(MongoDatabase db, String collection, String idfield, String id) {
        Document d = db.getCollection(collection).find(eq(idfield, id)).first();
        return d != null;
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
