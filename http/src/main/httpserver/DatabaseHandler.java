package httpserver;

import com.google.common.hash.Hashing;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.nio.charset.Charset;
import java.util.*;

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
    private MongoDatabase conversations;
    private boolean active;

    public DatabaseHandler() {
        try {
            client = MongoClients.create("mongodb://127.0.0.1:27017");
        } catch (MongoTimeoutException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        try {
            users = client.getDatabase("users");
            conversations = client.getDatabase("conversations");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

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
                    if (e.getKey().equals("password")) {
                        //Token needs to be updated
                        String pwd = (String)e.getValue();
                        String token = Hashing.sha512().hashString(uid + pwd, Charset.defaultCharset()).toString();
                        old.put("token", token);
                    }
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

            if (action == EditAction.ADD) {
                if (contacts.contains(contactID)) return 304;
                contacts.add(contactID);
            } else if (action == EditAction.DELETE){
                if (!contacts.contains(contactID)) return 202;
                contacts.remove(contactID);
            }

            users.getCollection("usercollection").updateOne(eq("username", uid), set("contacts", contacts));
            return 200;
        }
        return 410;
    }

    public String getUserField(String uid, String field) {
        Document d = getUserIfExists(uid);
        if (d != null) {
            return (String) d.get(field);
        } else {
            return "";
        }
    }

    public int createNewConversation(String[] members, boolean group) {
        for (String s : members) {
            if (getUserIfExists(s) == null) return 410;
        }

        Document d = new Document();
        d.append("isgroup", group)
                .append("members", Arrays.asList(members))
                .append("queue", new ArrayList<>());

        if (group) {
            d.append("image", "group_default");
        }

        conversations.getCollection("conversationcollection").insertOne(d);
        return 200;
    }

    public int editConversation(String id, Document newData) {
        Document old = getConversation(id);
        if (old == null) return 410;
        if (old.getBoolean("isgroup")) {
            Set<Map.Entry<String, Object>> entries = old.entrySet();
            for (Map.Entry e : entries) {
                if (newData.containsKey(e.getKey())) {
                    old.put((String) e.getKey(), newData.get(e.getKey()));
                }
            }

            conversations.getCollection("conversationcollection").replaceOne(eq("_id", new ObjectId(id)), old);
            return 200;
        } else {
            return 400;
        }
    }

    public List<String> getConversationMembers(String gid) {
        Document d = getConversation(gid);
        if (d != null) {
            return d.getList("members", String.class);
        }
        return null;
    }

    public int updateConversationMembers(String gid, List<String> membersMutation, EditAction action) {
        List<String> currentMembers = getConversationMembers(gid);
        if (currentMembers != null) {
            if (action == EditAction.ADD) {
                if (!currentMembers.addAll(membersMutation)) return 304;
            } else {
                if (!currentMembers.removeAll(membersMutation)) return 304;
            }

            conversations.getCollection("converstioncollection").updateOne(eq("_id", new ObjectId(gid)), set("members", currentMembers));
            return 200;
        } else {
            return 410;
        }
    }

    public String getConversationDetails(String gid) {
        Document d = getConversation(gid);
        if (d != null) {
            return d.toJson();
        }
        return "";
    }

    public String getConversationImage(String group) {
        Document d = getUserIfExists(group);
        if (d != null) {
            return (String) d.get("image");
        } else {
            return "";
        }
    }

    public boolean isActive() {
        return active;
    }

    private Document getUserIfExists(String uid) {
        FindIterable<Document> f = users.getCollection("usercollection").find(eq("username", uid)).limit(1);
        return f.first();
    }

    private Document getConversation(String gid) {
        ObjectId id = new ObjectId(gid);
        FindIterable<Document> f = conversations.getCollection("conversationcollection").find(eq("_id", id));
        return f.first();
    }

}
