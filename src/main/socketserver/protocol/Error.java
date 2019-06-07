package socketserver.protocol;

/**
 * Basic error message
 */
public class Error extends socketserver.protocol.BaseMessage {

    int CODE;
    String MESSAGE;

    public Error(String type, int code, String msg) {
        super(type);
        this.CODE = code;
        this.MESSAGE = msg;
    }
}
