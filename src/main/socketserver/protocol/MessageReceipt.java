package socketserver.protocol;

import socketserver.data.DataProvider;

/**
 * Sent/received as confirmation of a message
 */
public class MessageReceipt extends BaseMessage {

    int MessageID;

    public MessageReceipt(String type, int mid) {
        super(type);
        this.MessageID = mid;
    }

    @Override
    public void handle(DataProvider dp) {
        //Do we even have to do something?
    }
}
