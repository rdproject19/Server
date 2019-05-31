package data;

import protocol.Message;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ConversationCacheObject extends CacheObject {

    private Set<UserCacheObject> members;
    private String id;
    private Queue<Message> messageQueue;

    public ConversationCacheObject(String id, Set<UserCacheObject> members) {
        this.id = id;
        this.members = members;
        messageQueue = new LinkedBlockingQueue<>();
    }

    public void populateQueue(List<Message> messages) {
        messages.sort(Comparator.comparingLong(o -> o.timestamp));
        messages.forEach(m -> m.receivedBy = new HashSet<>());

        messageQueue.addAll(messages);
    }

    public void addMember(UserCacheObject member) {
        System.out.println("(WARNING) No members should be added after initialisation! Update the database instead!");
        this.members.add(member);
    }

    public void enqueueMessage(Message message) {
        messageQueue.add(message);
    }

    public Set<UserCacheObject> getMembers() {
        return members;
    }

}
