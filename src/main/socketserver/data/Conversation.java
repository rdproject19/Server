package socketserver.data;

import org.bson.Document;

import java.util.List;

/**
 * Basic representation of a conversation
 */
public class Conversation implements Queueable {

    String id;
    List<String> members;

    long cachedTime;

    /**
     * New conversation
     * @param id Conversation id
     * @param forCache Whether or not this conversation object is intended for caching
     */
    public Conversation(String id, boolean forCache) {
        this.id = id;
        if (forCache)
            cacheObjectAccessed();
    }

    /**
     * Returns the members of this conversation
     * @return A list of user ids
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * Get last time this object was touched in the cache
     * @return A unix timestamp
     */
    public long getCachedTime() {
        return cachedTime;
    }

    /**
     * Resets the cache time
     */
    public void cacheObjectAccessed() {
        cachedTime = System.currentTimeMillis();
    }

    /**
     * Parses a Conversation object from a BSON document
     * @param first The document to parse
     * @return The document as a Conversation object
     * @throws IllegalArgumentException if the conversation was invalid
     */
    public static Conversation fromDocument(Document first) {
        Conversation c = new Conversation(first.getObjectId("id").toString(), false);
        List<String> members = first.getList("members", String.class);
        if (members.size() >= 2) {
            c.members = members;
            return c;
        } else {
            throw new IllegalArgumentException("Conversation must contain at least two members");
        }
    }

    /**
     * Converts a conversation to a BSON document
     * @return A bson document
     */
    @Override
    public Document toDocument() {
        return new Document().append("id", id).append("members", members);
    }
}
