package server;

import com.google.gson.Gson;
import db.DataProvider;
import exceptions.MessageHandleException;
import protocol.*;

public class MessageHandler {

    private SocketServer serverInstance;

    private static final Gson gson = new Gson();

    private DataProvider dataProvider;

    public MessageHandler(SocketServer server, DataProvider d) {
        this.serverInstance = server;
        this.dataProvider = d;
    }

    public void receiveMessage(String raw) {
        BaseMessage msg = gson.fromJson(raw, BaseMessage.class);
        Class<? extends BaseMessage> msgtype = determineMessageType(msg.type);
        BaseMessage message = gson.fromJson(raw, msgtype);
        try {
            message.handle(dataProvider);
        } catch (MessageHandleException e) {
            System.out.println(e.toString());
        }
    }

    public static Class<? extends BaseMessage> determineMessageType(String type) {
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
