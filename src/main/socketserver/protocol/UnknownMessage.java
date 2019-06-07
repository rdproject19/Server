package socketserver.protocol;

import socketserver.data.DataProvider;
import socketserver.exceptions.UnknownMessageTypeException;

/**
 * Unknown message
 */
public class UnknownMessage extends BaseMessage {

    public UnknownMessage(String type) {
        super(type);
    }

    @Override
    public void handle(DataProvider dp) throws UnknownMessageTypeException {
        throw new UnknownMessageTypeException(this.TYPE);
    }
}
