package db;

import java.util.Set;

public class ConversationCacheObject extends CacheObject {

    private Set<UserCacheObject> members;
    private String id;

    public ConversationCacheObject(String id, Set<UserCacheObject> members) {
        this.id = id;
        this.members = members;
    }

    public void addMember(UserCacheObject member) {
        System.out.println("(WARNING) No members should be added after initialisation! Update the database instead!");
    }

    public Set<UserCacheObject> getMembers() {
        return members;
    }
}
