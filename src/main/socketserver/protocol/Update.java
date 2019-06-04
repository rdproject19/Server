package socketserver.protocol;

import socketserver.data.Conversation;

import java.util.List;

public class Update extends BaseMessage {

    public List<Conversation> NEW_CONVERSATIONS;
    public List<Message> NEW_MESSAGES;

    public Update(String type, List<Conversation> convos, List<Message> mssgs) {
        super(type);
        this.NEW_CONVERSATIONS = convos;
        this.NEW_MESSAGES = mssgs;
    }

    //No handle method, this message can only be sent
}
