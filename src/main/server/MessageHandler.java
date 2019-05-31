package server;

import com.google.gson.Gson;
import data.DataProvider;
import exceptions.MessageHandleException;
import org.java_websocket.WebSocket;
import protocol.*;

public class MessageHandler {

    private SocketServer serverInstance;

    private static final Gson gson = new Gson();

    private DataProvider dataProvider;

    public MessageHandler(SocketServer server, DataProvider d) {
        this.serverInstance = server;
        this.dataProvider = d;
    }

    /**
     * Handles a message
     * @param raw The raw message as it was received on the server
     */
    public void receiveMessage(String raw, WebSocket user) {
        BaseMessage msg = gson.fromJson(raw, BaseMessage.class);
        Class<? extends BaseMessage> msgtype = determineMessageType(msg.type);
        BaseMessage message = gson.fromJson(raw, msgtype);
        try {
            if (message instanceof Handshake) {
                ((Handshake)message).handle(dataProvider, user);
            } else {
                message.handle(dataProvider);
            }
        } catch (MessageHandleException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Determines the appropriate class representation for the message according to its type
     * @param type The type as provided in the message
     * @return A class object corresponding with type
     */
    public static Class<? extends BaseMessage> determineMessageType(String type) {
        switch (type) {
            case MessageTypes.MESSAGE:
                return Message.class;
            case MessageTypes.RECEIPT:
                return MessageReceipt.class;
            case MessageTypes.AUTHCHALLENGERESPONSE:
                return AuthChallengeResponse.class;
            case MessageTypes.HANDSHAKE:
                return Handshake.class;
            default:
                return UnknownMessage.class;
        }
    }
}
