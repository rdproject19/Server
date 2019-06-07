package socketserver.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.java_websocket.WebSocket;
import socketserver.data.DataProvider;
import socketserver.exceptions.MessageHandleException;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.protocol.Error;
import socketserver.protocol.*;

/**
 * Handles incoming messages
 */
public class MessageHandler {

    private static final Gson gson = new Gson();

    private DataProvider dataProvider;

    public MessageHandler(DataProvider d) {
        this.dataProvider = d;
    }

    /**
     * Handles a message
     * @param raw The raw message as it was received on the server
     */
    public void receiveMessage(String raw, WebSocket user) {
        BaseMessage msg = gson.fromJson(raw, BaseMessage.class);
        Class<? extends BaseMessage> msgtype = determineMessageType(msg.TYPE);
        BaseMessage message;
        try {
            message = gson.fromJson(raw, msgtype);
        } catch (JsonSyntaxException | IllegalStateException e) {
            try {
                user.send(new MessageFactory().setType("error").setStatusCode(400).setMessageString("JSON was malformed: " + e.getMessage()).getBody());
            } catch (UnknownMessageTypeException ex) {
                ex.printStackTrace();
            }
            return;
        }
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
            case MessageTypes.HANDSHAKE:
                return Handshake.class;
            case MessageTypes.ERROR:
                return Error.class;
            case MessageTypes.CONNECTION_SUCCESS:
                return socketserver.protocol.ConnectedMessage.class;
            case MessageTypes.DESYNC:
                return DesyncMessage.class;
            case MessageTypes.UPDATE:
                return Update.class;
            default:
                return UnknownMessage.class;
        }
    }
}
