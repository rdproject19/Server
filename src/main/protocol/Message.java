package protocol;

import data.DataProvider;
import data.UserConnection;
import server.MessageFactory;

import java.util.Set;

public class Message extends BaseMessage {

    public String id;
    public String uid;
    public String convid;
    int token;
    public long timestamp;
    String body;

    public Set<String> receivedBy;

    public Message(String type) {
        super(type);
    }

    @Override
    public void handle(DataProvider dp) {
        //Retrieve current token and compare;
        /*UserConnection obj = dp.getUserProfile(this.uid);
        obj.shiftLSFR();
        int serverToken = obj.getToken();
        MessageFactory fac = null;
        try {
            fac = new MessageFactory().setType("receipt")
                    .setMessageID(this.id);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (this.token > 0) {
            //Success
            dp.enqueueMessage(this);
            obj.getConnection().send(fac.setStatusCode(200).getBody());
        } else {
            obj.getConnection().send(fac.setStatusCode(409).getBody());
        }*/
    }
}
