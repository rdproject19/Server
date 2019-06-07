package socketserver.protocol;

import com.google.gson.Gson;
import org.bson.Document;
import socketserver.data.DataProvider;
import socketserver.data.Queueable;
import socketserver.data.UserConnection;
import socketserver.exceptions.ConversationNotFoundException;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.server.MessageFactory;

import java.util.ArrayList;
import java.util.List;

public class Message extends BaseMessage implements Queueable {

    public String SENDER_ID;
    public String CONVERSATION_ID;
    int MESSAGE_ID;
    long TIMESTAMP;
    public String MESSAGE;
    public boolean DELAYED;
    public long SEND_AT = 0;

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

            if (DELAYED && SEND_AT != 0 && SEND_AT > TIMESTAMP) {
                String[] schedule = new String[recipients.size()];
                recipients.toArray(schedule);
                dp.scheduleMessage(schedule, this);
            } else {
                List<String> enqueueFor = new ArrayList<>();
                for (String r : recipients) {
                    if (r.equals(SENDER_ID)) continue;

                    UserConnection recipientConnection = dp.getUser(r);
                    if (recipientConnection == null) {
                        //Enqueue message
                        enqueueFor.add(r);
                    } else {
                        //Send right away
                        recipientConnection.sendMessage(MessageFactory.fromProtocolObject(this));
                    }
                }

                String[] arr = new String[enqueueFor.size()];
                enqueueFor.toArray(arr);
                dp.enqueueMessage(arr, this);
            }

            //Send confirmation
            conn.sendMessage(new MessageFactory().setType("receipt").setMessageID(MESSAGE_ID).getBody());
        } catch (ConversationNotFoundException e) {
            conn.sendMessage(new MessageFactory().setType("error").setStatusCode(404).setMessageString("Conversation not found").getBody());
        }
    }

    @Override
    public Document toDocument() {
        return Document.parse(MessageFactory.fromProtocolObject(this));
    }

    public static Message fromDocument(Document document) {
        return new Gson().fromJson(document.toJson(), Message.class);
    }
}
