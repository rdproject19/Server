package protocol;

import data.DataProvider;
import data.UserConnection;
import exceptions.UnknownMessageTypeException;
import exceptions.UserNotFoundException;
import org.java_websocket.WebSocket;
import server.MessageFactory;

public class Handshake extends BaseMessage {

    public String USER_ID;
    public int AUTHENTICATION_TOKEN;

    public Handshake(String type) {
        super(type);
    }

    public void handle(DataProvider dp, WebSocket conn) {
        try {
            int serverToken = dp.getNewToken(USER_ID);
            if (AUTHENTICATION_TOKEN == serverToken) {
                //YEAH
                dp.addUser(USER_ID, conn);
            } else {
                //NO
                conn.send(
                    new MessageFactory().
                        setType("error").
                        setStatusCode(401).
                        setMessageString("Tokens didn't match").
                        getBody()
                );
            }
        } catch (UserNotFoundException | UnknownMessageTypeException ex) {
            //User not found
            try {
                conn.send(
                        new MessageFactory().
                                setType("error").
                                setStatusCode(404).
                                setMessageString("User not found").
                                getBody()
                );
            } catch (UnknownMessageTypeException e) {
                e.printStackTrace();
            }
        }
    }
}
