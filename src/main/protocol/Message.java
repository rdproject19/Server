package protocol;

<<<<<<< HEAD
import db.CacheManager;

public class Message extends BaseMessage {

    String uid;
    String convid;
    int token;
    long timestamp;
    String body;

    Message() { }

    @Override
    public void handle(CacheManager man) {
        //Retrieve current token and compare;
        int serverToken = 0;
        if (token == serverToken) {
            //Process the message;
        } else {
            //Send back an error;
        }
    }
=======
public class Message extends BaseMessage {
>>>>>>> fca15036019b012fd153030cfe8b1230138f3971
}
