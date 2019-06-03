package socketserver.server;

import socketserver.exceptions.UnknownMessageTypeException;
import org.junit.jupiter.api.Test;

class MessageFactoryTest {

    @Test
    public void testKnownMessageType() {
        try {
            MessageFactory fac = new MessageFactory().setType("unknown");
        } catch (UnknownMessageTypeException e) {
            e.printStackTrace();
        }
    }
}