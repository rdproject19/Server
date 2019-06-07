package socketserver.protocol;

/**
 * Sent when a user has successfully completed the handshake
 */
public class ConnectedMessage extends BaseMessage {

    int code;
    String message;

    public ConnectedMessage(String type, int code, String msg) {
        super(type);
        this.code = code;
        this.message = msg;
    }
}
