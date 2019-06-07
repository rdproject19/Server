package socketserver.protocol;

import org.java_websocket.WebSocket;
import socketserver.data.DataProvider;
import socketserver.exceptions.UnknownMessageTypeException;
import socketserver.exceptions.UserNotFoundException;
import socketserver.server.MessageFactory;
import socketserver.util.LSFR;

public class Handshake extends socketserver.protocol.BaseMessage {

    public String USER_ID;
    public int AUTHENTICATION_TOKEN;

    public Handshake(String type) {
        super(type);
    }

    public void handle(DataProvider dp, WebSocket conn) {
        try {
            LSFR l = dp.getLSFR(USER_ID);
            int serverToken = l.shift();
            if (AUTHENTICATION_TOKEN == serverToken /*|| AUTHENTICATION_TOKEN == 1337 /*- NICE BACKDOOR MAN*/) {
                //YEAH
                dp.addUser(USER_ID, conn);
                dp.shiftDBToken(USER_ID, l);
                conn.send(new MessageFactory().setType(MessageTypes.CONNECTION_SUCCESS).
                        setStatusCode(200).getBody());

                //Send updates
                conn.send(dp.createUpdate(USER_ID));
            } else {
                //NO
                conn.send(
                    new MessageFactory().
                        setType(socketserver.protocol.MessageTypes.ERROR).
                        setStatusCode(401).
                        setMessageString("Tokens didn't match").
                        getBody()
                );
                conn.close();
            }
        } catch (UserNotFoundException e) {
            //User not found
            try {
                conn.send(
                        new MessageFactory().
                                setType(socketserver.protocol.MessageTypes.ERROR).
                                setStatusCode(404).
                                setMessageString("User not found").
                                getBody()
                );
                conn.close();
            } catch (UnknownMessageTypeException ex) {
                e.printStackTrace();
            }
        } catch (UnknownMessageTypeException ex) {
            ex.printStackTrace();
        }
    }
}
