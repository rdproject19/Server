package protocol;

import db.CacheManager;
import db.DataProvider;
import db.UserCacheObject;

public class Message extends BaseMessage {

    public String uid;
    public String convid;
    int token;
    long timestamp;
    String body;

    Message() {
    }

    @Override
    public void handle(DataProvider dp) {
        //Retrieve current token and compare;
        UserCacheObject obj = dp.getUserProfile(this.uid);
        obj.shiftLSFR();
        int serverToken = obj.getToken();
        if (serverToken == this.token) {
            //Success
            dp.enqueueMessage(this);
        } else {
            //Error
        }
    }
}
