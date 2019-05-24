package java;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseHandler {

    MongoClient client;

    MongoDatabase users;
    MongoDatabase connections;

    public DatabaseHandler() {
        client = MongoClients.create("mongodb://127.0.0.1:27017");

        users = client.getDatabase("users");
        connections = client.getDatabase("connections");
    }

    public void createNewUser(String uname, String password, String nick, String token) {
        Document userDocument = new Document();
        userDocument.append("username", uname)
                .append("password", password)
                .append("fullname", nick)
                .append("token", token);
        long s = 0;
        userDocument.append("shiftcount", s);

        users.getCollection("usercollection").insertOne(userDocument);
    }

}
