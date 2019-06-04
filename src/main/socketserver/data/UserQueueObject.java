package socketserver.data;

import org.bson.Document;
import socketserver.protocol.Message;

public class UserQueueObject implements Queueable {

    private String type;
    private Queueable data;

    private int recipients;
    private int receivedBy;

    public UserQueueObject(String type, int numofrecipients, Queueable data) {
        this.type = type;
        this.data = data;
        this.recipients = numofrecipients;
    }

    public Document toDocument() {
        Document newDocument = new Document();
        newDocument.append("type", type)
                .append("recipients", recipients)
                .append("received", receivedBy)
                .append("data", data.toDocument());

        return newDocument;
    }

    public static UserQueueObject fromDocument(Document document) {
        String type = document.getString("type");
        Document data = (Document) document.get("data");

        int receivedBy = document.getInteger("received");

        UserQueueObject q;
        if (type.equals("message")) {
            q = new UserQueueObject(type, receivedBy, Message.fromDocument(data));
        } else {
            q = new UserQueueObject(type, receivedBy, Conversation.fromDocument(data));
        }
        q.recipients = document.getInteger("recipients");

        return q;
    }

    public String getType() {
        return type;
    }

    public boolean receivedByAll() {
        return recipients >= receivedBy + 1;
    }

    public Queueable getData() {
        return data;
    }
}
