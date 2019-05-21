package protocol;

import db.CacheManager;

public class BaseMessage {
    public String type;

    public boolean responseAvailable;
    public String response;

    public void handle(CacheManager man) {

    }
}
