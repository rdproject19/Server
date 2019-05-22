package protocol;

import db.DataProvider;
import exceptions.BaseTypeHandleException;
import exceptions.MessageHandleException;

public class BaseMessage {
    public String type;

    public void handle(DataProvider dp) throws MessageHandleException {
        throw new BaseTypeHandleException();
    }
}
