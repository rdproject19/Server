package socketserver.protocol;

import socketserver.data.DataProvider;
import socketserver.data.UserConnection;
import socketserver.exceptions.ConversationNotFoundException;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.server.MessageFactory;

import java.util.List;

public class Message extends BaseMessage {

    public String SENDER_ID;
    public String CONVERSATION_ID;
    int MESSAGE_ID;
    long TIMESTAMP;
    String MESSAGE;

    public Message(String type) {
        super(type);
    }

    @Override
    public void handle(DataProvider dp) throws UserNotFoundException, UnknownMessageTypeException {
        UserConnection conn = dp.getUser(SENDER_ID);
        if (conn == null) {
            return;
        }

        try {
            List<String> recipients = dp.getConversation(CONVERSATION_ID).getMembers();

            for (String r : recipients) {
                if (r.equals(SENDER_ID)) continue;
               UserConnection recipientConnection = dp.getUser(SENDER_ID);
               if (recipientConnection == null) {
                   //Enqueue message
                    dp.enqueueMessage(r, this);
               } else {
                   //Send right away
                   recipientConnection.getConnection().send(MessageFactory.fromProtocolObject(this));
               }
            }

            //Send confirmation
            conn.getConnection().send(new MessageFactory().setType("receipt").setMessageID(MESSAGE_ID).getBody());
        } catch (ConversationNotFoundException e) {
            conn.getConnection().send(new MessageFactory().setType("error").setStatusCode(404).setMessageString("Conversation not found").getBody());
        }
    }
}
