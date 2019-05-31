package data;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import exceptions.UserNotFoundException;
import org.bson.Document;
import util.LSFR;

import static com.mongodb.client.model.Filters.eq;

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

    public void updateLSFR(LSFR l) {

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
