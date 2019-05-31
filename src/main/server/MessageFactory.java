package server;

import com.google.gson.Gson;
import exceptions.UnknownMessageTypeException;
import protocol.*;
import protocol.Error;

/**
 * Simple message factory
 */
public class MessageFactory {

    private static final Gson gson = new Gson();

    Class<? extends BaseMessage> instance;
    private String type;
    private int code;
    private String messageid;
    private String msg;

    /**
     * Set message type
     * @param type Type of the message.
     * @return The factory
     * @throws UnknownMessageTypeException
     */
    public MessageFactory setType(String type) throws UnknownMessageTypeException {
        instance = MessageHandler.determineMessageType(type);
        this.type = type;
        return this;
    }

    /**
     * Only when building a receipt
     * @param code Status code to assign this message
     * @return The factory
     */
    public MessageFactory setStatusCode(int code) {
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
     * Sets the message (for errors)
     * @param msg msg the error
     * @return The factory
     */
    public MessageFactory setMessageString(String msg) {
        this.msg = msg;
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
            case MessageTypes.ERROR:
                return new Error(type, code, msg);
            default:
                return new UnknownMessage(type);
        }
    }

}
