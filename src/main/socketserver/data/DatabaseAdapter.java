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

/**
 * Handles database IO
 */
public class DatabaseAdapter {

    //Parameters
    private static final String USER_DB_NAME = "users";
    private static final String CONVERSATION_DB_NAME = "conversations";

    //Actual connection
    private final MongoClient connection;

    //Databases
    private MongoDatabase users = null;
    private MongoDatabase conversations = null;

    /**
     * Creates a new DatabaseAdapter
     * @param host Hostname for the MongoDB instance
     * @param port Port for the MongoDB instance
     */
    public DatabaseAdapter(String host, int port) {
        connection = MongoClients.create("mongodb://" + host + ":" + port);
        if (!initDatabases()) {
            System.out.println("FATAL - One of the required databases does not exist! Please check configuration.");
            System.exit(-2);
        }
    }

    /**
     * Gets a users LSFR from the database
     * @param uid The uid of the user
     * @return An LSFR, popualated with the current state for given user.
     * @throws UserNotFoundException If the user was not found
     */
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

    /**
     * Updates the users LSFR in the database
     * @param id The user id
     * @param l The new LSFR
     */
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

    /**
     * Resets the LSFR (desync procedure)
     * @param id The user id
     * @param newcount The new (synchronized) shiftcount
     */
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

    /**
     * Checks if the user exists in the database
     * @param uid User id
     * @throws UserNotFoundException If the user does not exist
     */
    public void userExists(String uid) throws UserNotFoundException {
        FindIterable user = users.getCollection("usercollection").find(eq("username", uid));
        if (user.first() == null) throw new UserNotFoundException(uid);
    }

    /**
     * Gets a conversation from the database
     * @param gid The conversation id
     * @return The conversation associated with given id
     * @throws ConversationNotFoundException If the conversation does not exist
     */
    public Conversation getConversation(String gid) throws ConversationNotFoundException {
        FindIterable conv = conversations.getCollection("conversationcollection").find(eq("_id", new ObjectId(gid)));
        if (conv.first() == null) {
            throw new ConversationNotFoundException(gid);
        } else {
            return Conversation.fromDocument((Document) conv.first());
        }
    }

    /**
     * Queues a message for given users
     * @param ids The user ids for whom the message must be queued
     * @param data The object to be queued.
     */
    public void queueMessage(String[] ids, UserQueueObject data) {
        Document d = data.toDocument();
        conversations.getCollection("queue").insertOne(d);

        ObjectId oid = d.getObjectId("_id");

        users.getCollection("usercollection").updateMany(in("username", ids), addToSet("queue", oid));
    }

    /**
     * Gets a users queue
     * @param id The user id for whom to get the queue
     * @return A list of {@link UserQueueObject} corresponding to the users queue.
     * @throws QueueObjectNotFoundException If an object enqueued for the user does not actually exist in the queue collection
     */
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

    /**
     * Checks whether an object exists
     * @param db The database to check
     * @param collection The collection to check
     * @param idfield The field that must be checked
     * @param id The value for idfield which must exist in db.collection
     * @return True if the document exists, false otherwise
     */
    public boolean documentExists(MongoDatabase db, String collection, String idfield, String id) {
        Document d = db.getCollection(collection).find(eq(idfield, id)).first();
        return d != null;
    }

    /**
     * Initializes the databases
     * @return False if one of the databases does not exists. True otherwise.
     */
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
