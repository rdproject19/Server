package db;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

public class DatabaseServerConnector {

    private MongoClient client;
    private String host;
    private int port;

    private boolean isConnected = false;

    public DatabaseServerConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connects the connector to the MongoDB database
     */
    public void setup() {
        client = MongoClients.create(
                MongoClientSettings.builder()
                    .applyToClusterSettings(
                            builder -> builder.hosts(Arrays.asList(new ServerAddress(host, port)))
                    )
                .build()
        );
        isConnected = true;
    }

    /**
     * Get a database from the MongoDB server
     * @param name The name of the database to be served
     * @return The requested database
     */
    public MongoDatabase serveDatabase(String name) {
        return client.getDatabase(name);
    }

    public boolean isConnected() {
        return isConnected;
    }

}
