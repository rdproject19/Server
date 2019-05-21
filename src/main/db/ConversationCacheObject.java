package db;

import java.util.Set;

public class ConversationCacheObject extends CacheObject {

    private Set<UserCacheObject> members;

    public Set<UserCacheObject> getMembers() {
        return members;
    }
}
