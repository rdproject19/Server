package data;

import exceptions.ConversationNotFoundException;
import exceptions.UserNotFoundException;
import protocol.Message;

import java.util.Set;

/**
 * Manages data I/O.
 * I.E. Checks if requested items are cached, and if not, fetches them from the database.
 */
public class DataProvider {

    public CacheManager cache;

    public DataProvider() {
        this.cache = new CacheManager();
    }

    public UserCacheObject getUserProfile(String uid) {
        try {
            UserCacheObject cacheObject = cache.getUser(uid);
            return cacheObject;
        } catch (UserNotFoundException ex) {
            //Fetch from DB;
            ex.printStackTrace();
        }
        return null;
    }

    public void enqueueMessage(Message message) {
        Set<UserCacheObject> recipients = getConversationMembers(message.convid);
        for (UserCacheObject u : recipients) {
            UserCacheObject cacheObject = getUserProfile(message.uid);
            if (cacheObject.getConnection() != null) {
                //TODO: Implement actual message encoder. Of course, first a challenge would have to be provided
            } else {
                //Write to data
            }
        }
    }

    public Set<UserCacheObject> getConversationMembers(String cid) {
        try {
            Set<UserCacheObject> m = cache.getConversationMembers(cid);
            return m;
        } catch (ConversationNotFoundException ex) {
            //Fetch from DB;
        }
        return null;
    }
}
