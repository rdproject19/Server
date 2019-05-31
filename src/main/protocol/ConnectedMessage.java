package protocol;

public class ConnectedMessage extends BaseMessage {

    int code;
    String message;

    public ConnectedMessage(String type, int code, String msg) {
        super(type);
        this.code = code;
        this.message = msg;
    }
}
