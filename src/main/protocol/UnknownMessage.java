package protocol;

import db.DataProvider;
import exceptions.UnknownMessageTypeException;

public class UnknownMessage extends BaseMessage {

    public UnknownMessage(String type) {
        super(type);
    }

    @Override
    public void handle(DataProvider dp) throws UnknownMessageTypeException {
        throw new UnknownMessageTypeException(this.type);
    }
}
