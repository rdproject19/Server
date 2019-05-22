package server;

import com.google.gson.Gson;
import exceptions.UnknownMessageTypeException;
import protocol.*;

public class MessageFactory {

    private static final Gson gson = new Gson();

    Class<? extends BaseMessage> instance;
    private String type;
    private int code;
    private String messageid;

    public MessageFactory setType(String type) throws UnknownMessageTypeException {
        if (type.equals(MessageTypes.RECEIPT)) {
            this.type = type;
            instance = MessageHandler.determineMessageType(type);
        } else {
            throw new UnknownMessageTypeException(type);
        }
        return this;
    }

    public MessageFactory setStatusCode(int code) {
        if (!instance.getCanonicalName().equals("protocol.MessageReceipt")) return null;
        this.code = code;
        return this;
    }

    public MessageFactory setMessageID(String id) {
        this.messageid = id;
        return this;
    }

    public String getBody() {
        BaseMessage msg = buildMessageClass();
        return gson.toJson(msg, instance);
    }

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
