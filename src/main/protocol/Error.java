package protocol;

public class Error extends BaseMessage {

    int code;
    String message;

    public Error(String type) {
        super(type);
    }
}
