package protocol;

import data.DataProvider;
import data.UserConnection;
import exceptions.UnknownMessageTypeException;
import exceptions.UserNotFoundException;
import org.java_websocket.WebSocket;
import server.MessageFactory;
import util.LSFR;

public class Handshake extends BaseMessage {

    public String USER_ID;
    public int AUTHENTICATION_TOKEN;

    public Handshake(String type) {
        super(type);
    }

    public void handle(DataProvider dp, WebSocket conn) {
        try {
            LSFR l = dp.getLSFR(USER_ID);
            int serverToken = l.shift();
            if (AUTHENTICATION_TOKEN == serverToken) {
                //YEAH
                dp.addUser(USER_ID, conn);
                dp.shiftDBToken(USER_ID, l);
                conn.send(new MessageFactory().setType(MessageTypes.CONNECTION_SUCCESS).
                        setStatusCode(200).getBody());
            } else {
                //NO
                conn.send(
                    new MessageFactory().
                        setType(MessageTypes.ERROR).
                        setStatusCode(401).
                        setMessageString("Tokens didn't match").
                        getBody()
                );
            }
        } catch (UserNotFoundException e) {
            //User not found
            try {
                conn.send(
                        new MessageFactory().
                                setType(MessageTypes.ERROR).
                                setStatusCode(404).
                                setMessageString("User not found").
                                getBody()
                );
            } catch (UnknownMessageTypeException ex) {
                e.printStackTrace();
            }
        } catch (UnknownMessageTypeException ex) {
            ex.printStackTrace();
        }
    }
}
