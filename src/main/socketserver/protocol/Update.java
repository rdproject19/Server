package socketserver.protocol;

import socketserver.data.Conversation;
import socketserver.data.DataProvider;
import socketserver.data.UserConnection;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.exceptions.UserNotFoundException;

import java.util.List;

public class Update extends BaseMessage {

    public String USER_ID;

    public List<Conversation> NEW_CONVERSATIONS;
    public List<Message> NEW_MESSAGES;

    public Update(String type, List<Conversation> convos, List<Message> mssgs) {
        super(type);
        this.NEW_CONVERSATIONS = convos;
        this.NEW_MESSAGES = mssgs;
    }

    //Handle, only if its an update request
    @Override
    public void handle(DataProvider dp) {
        try {
            UserConnection conn = dp.getUser(USER_ID);

            try {
                String update = dp.createUpdate(USER_ID);
                conn.getConnection().send(update);
            } catch (UnknownMessageTypeException e) {
                e.printStackTrace() ;
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }
}
