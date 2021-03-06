package socketserver.exceptions;

/**
 * Thrown if a message of unknown type if received
 */
public class UnknownMessageTypeException extends MessageHandleException {
    String type;
    public UnknownMessageTypeException(String t) {
        this.type = t;
    }

    @Override
    public String toString() {
        return "No action available for message of type " + type;
    }
}
