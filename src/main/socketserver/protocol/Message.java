package socketserver.protocol;

import socketserver.data.DataProvider;
import socketserver.data.UserConnection;
import socketserver.exceptions.ConversationNotFoundException;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.server.MessageFactory;

import java.util.List;

public class Message extends BaseMessage {

    public String id;
    public String uid;
    public String convid;
    public long timestamp;
    String body;

    public Message(String type) {
        super(type);
    }

    @Override
    public void handle(DataProvider dp) throws UserNotFoundException, UnknownMessageTypeException {
        UserConnection conn = dp.getUser(uid);
        if (conn == null) {
            return;
        }

        try {
            List<String> recipients = dp.getConversation(convid).getMembers();

            for (String r : recipients) {
               UserConnection recipientConnection = dp.getUser(uid);
               if (recipientConnection == null) {
                   //Enqueue message
                    dp.enqueueMessage(r, this);
               } else {
                   //Send right away
                   conn.getConnection().send(MessageFactory.fromProtocolObject(this));
               }
            }

        } catch (ConversationNotFoundException e) {
            conn.getConnection().send(new MessageFactory().setType("error").setStatusCode(404).setMessageString("Conversation not found").getBody());
        }
    }
}
