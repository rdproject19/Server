package socketserver.data;

import org.bson.Document;

import java.util.List;

public class Conversation implements Queueable {

    String id;
    List<String> members;

    public Conversation(String id) {
        this.id = id;
    }

    public List<String> getMembers() {
        return members;
    }

    public static Conversation fromDocument(Document first) {
        Conversation c = new Conversation(first.getObjectId("_id").toString());
        List<String> members = first.getList("members", String.class);
        if (members.size() >= 2) {
            c.members = members;
            return c;
        } else {
            throw new IllegalArgumentException("Conversation must contain at least two members");
        }
    }

    @Override
    public Document toDocument() {
        return new Document().append("id", id).append("members", members);
    }
}
