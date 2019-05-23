package protocol;

import data.DataProvider;
import exceptions.BaseTypeHandleException;
import exceptions.MessageHandleException;

public class BaseMessage {
    public String type;

    /**
     * Handles the message
     * @param dp Dataprovider
     * @throws MessageHandleException
     */
    public void handle(DataProvider dp) throws MessageHandleException {
        throw new BaseTypeHandleException();
    }

    public BaseMessage(String type) {
        this.type = type;
    }
}
