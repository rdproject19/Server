package socketserver.protocol;

public class Error extends socketserver.protocol.BaseMessage {

    int code;
    String message;

    public Error(String type, int code, String msg) {
        super(type);
        this.code = code;
        this.message = msg;
    }
}
