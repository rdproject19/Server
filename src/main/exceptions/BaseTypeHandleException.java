package exceptions;

public class BaseTypeHandleException extends MessageHandleException {
    @Override
    public String toString() {
        return "Illegal attempt to call handle on BaseMessage";
    }
}
