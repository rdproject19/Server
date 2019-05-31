package server;

import exceptions.UnknownMessageTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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