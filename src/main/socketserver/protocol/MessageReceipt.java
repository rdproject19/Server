package socketserver.protocol;

import socketserver.data.DataProvider;

public class MessageReceipt extends BaseMessage {

    public String MessageID;

    public MessageReceipt(String type, String mid) {
        super(type);
        this.MessageID = mid;
    }

    @Override
    public void handle(DataProvider dp) {
        //Do we even have to do something?
    }
}
