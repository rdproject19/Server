package db;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

public class DatabaseConnector {

    private MongoClient client;
    private String host;
    private int port;

    private MongoDatabase db;
    private String dbname;

    public DatabaseConnector(String host, int port, String dbname) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
    }

    public void setup() {
        client = MongoClients.create(
                MongoClientSettings.builder()
                    .applyToClusterSettings(
                            builder -> builder.hosts(Arrays.asList(new ServerAddress(host, port)))
                    )
                .build()
        );

        db = client.getDatabase(dbname);
    }


}
