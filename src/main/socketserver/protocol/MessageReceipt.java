package socketserver.protocol;

import socketserver.data.DataProvider;

public class MessageReceipt extends BaseMessage {

    public String MessageID;
    public String UserID;
    public int code;

    public MessageReceipt(String mid, String uid, int code) {
        super(MessageTypes.RECEIPT);
        this.MessageID = mid;
        this.UserID = uid;
        this.code = code;
    }

    public MessageReceipt(String mid, int code) {
        this(mid, "", code);
    }

    @Override
    public void handle(DataProvider dp) {

    }
}
