package socketserver.exceptions;

/**
 * Thrown if it is attempted to handle a base message
 */
public class BaseTypeHandleException extends MessageHandleException {
    @Override
    public String toString() {
        return "Illegal attempt to call handle on BaseMessage";
    }
}
