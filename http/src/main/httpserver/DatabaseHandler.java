package httpserver;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class DatabaseHandler {

    public enum EditAction {
        ADD,
        DELETE
    }

    private MongoClient client;

    private MongoDatabase users;
    private MongoDatabase connections;
    private boolean active;

    public DatabaseHandler() {
        client = MongoClients.create("mongodb://127.0.0.1:27017");

        users = client.getDatabase("users");
        connections = client.getDatabase("connections");

        active = true;
    }

    public int createNewUser(String uname, String password, String nick, String token, String image) {
        if (getUserIfExists(uname) != null) return 409; //Conflict

        List<String> emptyContacts = new ArrayList<>();
        Document userDocument = new Document();
        userDocument.append("username", uname)
                .append("password", password)
                .append("fullname", nick)
                .append("token", token)
                .append("contacts", emptyContacts)
                .append("image", image);
        long s = 0;
        userDocument.append("shiftcount", s);

        users.getCollection("usercollection").insertOne(userDocument);
        return 200; //OK
    }

    public boolean updateUser(String uid, Document newData) {
        Document old = getUserIfExists(uid);
        if (old != null) {

            Set<Map.Entry<String, Object>> entries = old.entrySet();
            for (Map.Entry e : entries) {
                if (newData.containsKey(e.getKey())) {
                    old.put((String) e.getKey(), newData.get(e.getKey()));
                }
            }

            users.getCollection("usercollection").replaceOne(eq("username", uid), old);
            return true;
        }
        return false;
    }

    public boolean checkUserCredentials(String uid, String password) {
        FindIterable<Document> f = users.getCollection("usercollection").find(and(eq("username", uid), eq("password", password)));
        for (Document d : f) {
            if (d != null) {
                return true;
            }
        }
        return false;
    }

    public List<String> getContacts(String uid) {
        Document user = getUserIfExists(uid);
        if (user != null) {
            return user.getList("contacts", String.class);
        }
        return null;
    }

    public int editContacts(String uid, String contactID, EditAction action) {
        if (getUserIfExists(uid) != null) {
            List<String> contacts = getContacts(uid);

            if (contacts == null) return 202;
            if (!contacts.contains(contactID)) return 202;

            if (action == EditAction.ADD) {
                contacts.add(contactID);
            } else if (action == EditAction.DELETE){
                contacts.remove(contactID);
            }

            users.getCollection("usercollection").updateOne(eq("username", uid), set("contacts", contacts));
            return 200;
        }
        return 410;
    }

    public String getUserImage(String uid) {
        Document d = getUserIfExists(uid);
        if (d != null) {
            return (String) d.get("image");
        } else {
            return "";
        }
    }

    public int createNewConversation() {
        return 0;
    }

    public boolean isActive() {
        return active;
    }

    private Document getUserIfExists(String uid) {
        FindIterable<Document> f = users.getCollection("usercollection").find(eq("username", uid)).limit(1);
        return f.first();
    }

}
