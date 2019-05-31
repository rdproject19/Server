package data;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import exceptions.ConversationNotFoundException;
import exceptions.UserNotFoundException;
import org.bson.Document;
import util.LSFR;

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

    public UserCacheObject getUser(String uid) throws UserNotFoundException {
        FindIterable it = users.getCollection("usercollection").find(eq("username", uid));
        Document user = (Document) it.first();
        if (user != null) {
            String token = user.getString("token");
            LSFR l = new LSFR(token, user.getLong("shiftcount"));
            return new UserCacheObject(uid, l, null);
        } else {
            throw new UserNotFoundException(uid);
        }
    }

    public ConversationCacheObject getConversation(String gid) throws ConversationNotFoundException {
        FindIterable it = conversations.getCollection("conversationcollection").find(eq("_id", gid));
        Document user = (Document) it.first();
        if (user != null) {

        } else {

        }
        return null;
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
