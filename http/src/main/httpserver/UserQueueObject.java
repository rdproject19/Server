package httpserver;

import org.bson.Document;

public class UserQueueObject {

    private String type;
    private Conversation data;

    private int recipients;
    private int receivedBy;

    public UserQueueObject(String type, int numofrecipients, Conversation data) {
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
}
