package socketserver.data;

import org.bson.Document;
import socketserver.protocol.Message;

/**
 * Simple object representing a queued object
 */
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

    /**
     * Converts current instance to BSON object
     * @return BSON representation of current instance
     */
    public Document toDocument() {
        Document newDocument = new Document();
        newDocument.append("type", type)
                .append("recipients", recipients)
                .append("received", receivedBy)
                .append("data", data.toDocument());

        return newDocument;
    }

    /**
     * Parses a BSON document to a UserQueueObject
     * @param document The document to parse
     * @return The parsed UserQueueObject
     */
    public static UserQueueObject fromDocument(Document document) {
        String type = document.getString("type");
        Document data = (Document) document.get("data");

        int numrecipients = document.getInteger("recipients");

        UserQueueObject q;
        if (type.equals("message")) {
            q = new UserQueueObject(type, numrecipients, Message.fromDocument(data));
        } else {
            q = new UserQueueObject(type, numrecipients, Conversation.fromDocument(data));
        }
        q.receivedBy = document.getInteger("received");

        return q;
    }

    public String getType() {
        return type;
    }

    public boolean receivedByAll() {
        return recipients <= (receivedBy + 1);
    }

    public Queueable getData() {
        return data;
    }
}
