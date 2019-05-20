package server;

import com.google.gson.Gson;
import db.CacheManager;
import protocol.*;

public class MessageHandler {

    private SocketServer serverInstance;

    private static final Gson gson = new Gson();

    public MessageHandler(SocketServer server) {
        this.serverInstance = server;
    }

    public void receiveMessage(String raw) {
        BaseMessage msg = gson.fromJson(raw, BaseMessage.class);
        Class<? extends BaseMessage> msgtype = determineMessageType(msg.type);
        BaseMessage message = gson.fromJson(raw, msgtype);
        message.handle(new CacheManager());
        if (message.responseAvailable) {
            //serverInstance.send(message.response)
        }
    }

    public Class<? extends BaseMessage> determineMessageType(String type) {
        switch (type) {
            case MessageTypes.MESSAGE:
                return Message.class;
            case MessageTypes.RECEIPT:
                return MessageReceipt.class;
            case MessageTypes.AUTHCHALLENGERESPONSE:
                return AuthChallengeResponse.class;
            default:
                return UnknownMessage.class;
        }
    }
}
