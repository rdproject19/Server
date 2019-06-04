package socketserver.server;

import com.google.gson.Gson;
import socketserver.data.Conversation;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.protocol.Error;
import socketserver.protocol.*;

import java.util.List;

/**
 * Simple message factory
 */
public class MessageFactory {

    private static final Gson gson = new Gson();
    private String type;
    private int code;
    private String msg = "";
    private int messageid;
    private List<Conversation> newconv;
    private List<Message> newmessg;
    /**
     * Set message type
     * @param type Type of the message.
     * @return The factory
     * @throws UnknownMessageTypeException
     */
    public MessageFactory setType(String type) throws UnknownMessageTypeException {
        this.type = type;
        Class<? extends BaseMessage> msg = socketserver.server.MessageHandler.determineMessageType(type);
        if (msg.getTypeName().equals("socketserver.UnknownMessagenknownMessage")) throw new UnknownMessageTypeException(type);
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
     * Sets the message (for errors)
     * @param msg msg the error
     * @return The factory
     */
    public MessageFactory setMessageString(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * Sets the new conversation list (for updates)
     * @param c The list
     * @return The factory
     */
    public MessageFactory setNewConversations(List<Conversation> c) {
        this.newconv = c;
        return this;
    }

    /**
     * Sets the new message list (for updates)
     * @param m The list
     * @return The factory
     */
    public MessageFactory setNewMessages(List<Message> m) {
        this.newmessg = m;
        return this;
    }

    public MessageFactory setMessageID(int messageID) {
        this.messageid = messageID;
        return this;
    }

    /**
     * @return A string json representation of the message
     */
    public String getBody() {
        BaseMessage msg = buildMessageClass();
        return gson.toJson(msg, msg.getClass());
    }

    /**
     * If something can be directly echoed (like a message), this function can be used to turn a class into json
     * @param object The object to be serialized
     * @param <T> Must extends BaseMessage
     * @return JSON representation of the provided object
     */
    public static <T extends BaseMessage> String fromProtocolObject(T object) {
        return gson.toJson(object);
    }

    /**
     * Converts the factory into a class based representation
     * @return The class based representation of the message represented in the factory
     */
    private BaseMessage buildMessageClass() {
        switch (type) {
            case MessageTypes.MESSAGE:
                return new Message(type);
            case MessageTypes.UPDATE:
                return new Update(type, newconv, newmessg);
            case MessageTypes.ERROR:
                return new Error(type, code, msg);
            case MessageTypes.CONNECTION_SUCCESS:
                return new ConnectedMessage(type, code, msg);
            case MessageTypes.RECEIPT:
                return new MessageReceipt(type, messageid);
            default:
                return new UnknownMessage(type);
        }
    }

}
