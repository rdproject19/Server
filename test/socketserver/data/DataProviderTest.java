package socketserver.data;

import com.google.common.hash.Hashing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import socketserver.protocol.Message;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataProviderTest {

    DataProvider provider;

    @BeforeAll
    public void setup() {
        provider = new DataProvider(new DatabaseAdapter("127.0.0.1", 27017));
    }

    @Test
    public void testQueuing() {
        Message message = new Message("message");
        message.MESSAGE = "HALLO";
        String[] received = new String[2];
        received[0] = "koen1";
        received[1] = "koen";

        provider.enqueueMessage(received, message);
    }

    @Test
    public void testUpdate() {
        //String res = provider.createUpdate("koen1");
        //System.out.print(res);
        //assertNotEquals(res, "{\"NEW_CONVERSATIONS\":[],\"NEW_MESSAGES\":[],\"TYPE\":\"update\"}");
    }
}