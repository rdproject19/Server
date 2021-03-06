package socketserver.protocol;

import socketserver.data.DataProvider;

/**
 * Received if LSFRs have been desynchronized
 */
public class DesyncMessage extends BaseMessage {

    long COUNT;
    String USER_ID;

    public DesyncMessage(String type) {
        super(type);
    }

    @Override
    public void handle(DataProvider dp) {
        System.out.println("");
        dp.resetDBToken(USER_ID, COUNT);
    }
}
