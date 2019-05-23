package server;

import com.google.gson.Gson;
import exceptions.UnknownMessageTypeException;
import protocol.*;

/**
 * Simple message factory
 */
public class MessageFactory {

    private static final Gson gson = new Gson();

    Class<? extends BaseMessage> instance;
    private String type;
    private int code;
    private String messageid;

    /**
     * Set message type
     * @param type Type of the message.
     * @return The factory
     * @throws UnknownMessageTypeException
     */
    public MessageFactory setType(String type) throws UnknownMessageTypeException {
        if (type.equals(MessageTypes.RECEIPT)) {
            this.type = type;
            instance = MessageHandler.determineMessageType(type);
        } else {
            throw new UnknownMessageTypeException(type);
        }
        return this;
    }

    /**
     * Only when building a receipt
     * @param code Status code to assign this message
     * @return The factory
     */
    public MessageFactory setStatusCode(int code) {
        if (!instance.getCanonicalName().equals("protocol.MessageReceipt")) return null;
        this.code = code;
        return this;
    }

    /**
     * Sets message id
     * @param id The message id
     * @return The factory
     */
    public MessageFactory setMessageID(String id) {
        this.messageid = id;
        return this;
    }

    /**
     * @return A string json representation of the message
     */
    public String getBody() {
        BaseMessage msg = buildMessageClass();
        return gson.toJson(msg, instance);
    }

    /**
     * Converts the factory into a class based representation
     * @return The class based representation of the message represented in the factory
     */
    private BaseMessage buildMessageClass() {
        switch (type) {
            case MessageTypes.MESSAGE:
                return new Message(type);
            case MessageTypes.RECEIPT:
                return new MessageReceipt(this.messageid, this.code);
            case MessageTypes.AUTHCHALLENGERESPONSE:
                return new AuthChallengeResponse(type);
            default:
                return new UnknownMessage(type);
        }
    }

}
