package socketserver.protocol;

import socketserver.data.DataProvider;
import socketserver.exceptions.BaseTypeHandleException;
import socketserver.exceptions.MessageHandleException;

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
