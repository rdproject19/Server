package db;

import exceptions.ConversationNotFoundException;
import exceptions.UserNotFoundException;
import org.java_websocket.WebSocketImpl;
import util.LSFR;

import java.util.HashMap;
import java.util.Set;

public class CacheManager {

    private HashMap<String, UserCacheObject> userCache = new HashMap<>();
    private HashMap<String, ConversationCacheObject> conversationCache = new HashMap<>();

    public void addUser(String id, String token, WebSocketImpl connection) {
        LSFR lsfr = new LSFR(token);
        UserCacheObject c = new UserCacheObject(id, lsfr, connection);
        userCache.put(id, c);
    }

    public void addUser(String id, String token, long shift, WebSocketImpl connection) {
        LSFR lsfr = new LSFR(token, shift);
        UserCacheObject c = new UserCacheObject(id, lsfr, connection);
        userCache.put(id, c);
    }

    public UserCacheObject getUser(String uid) throws UserNotFoundException {
        UserCacheObject obj = userCache.get(uid);
        if (obj != null) {
            return obj;
        } else {
            throw new UserNotFoundException(uid);
        }
    }

    public Set<UserCacheObject> getConversationMembers(String id) throws ConversationNotFoundException {
        ConversationCacheObject obj = conversationCache.get(id);
        if (obj != null) {
            return obj.getMembers();
        } else {
            throw new ConversationNotFoundException(id);
        }
    }

}
