package db;

import com.mongodb.client.MongoDatabase;

public class DatabaseConnector {

    private DatabaseServerConnector connection;

    private MongoDatabase db;

    public DatabaseConnector(DatabaseServerConnector conn, String dbname) {
        this.connection = conn;
        if (!connection.isConnected()) {
            connection.setup();
        }
        db = connection.serveDatabase(dbname);
    }

}
