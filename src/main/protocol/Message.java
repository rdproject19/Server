package protocol;

import db.CacheManager;

public class Message extends BaseMessage {

    public String uid;
    public String convid;
    int token;
    long timestamp;
    String body;

    Message() {
    }

    @Override
    public void handle(CacheManager man) {
        //Retrieve current token and compare;
        int serverToken = 0;
        if (token == serverToken) {

        } else {
            //Send back an error;
        }
    }
}
