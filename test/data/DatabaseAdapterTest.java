package data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseAdapterTest {

    @Test
    public void testDatabaseConnector() {
        DatabaseAdapter db = new DatabaseAdapter("127.0.0.1", 27017);

    }

}