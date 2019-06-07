package socketserver.exceptions;

/**
 * Thrown if a conversation does not exist
 */
public class ConversationNotFoundException extends Exception {
    private String id;
    public ConversationNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "No conversation with ID: " + this.id + " exists";
    }
}
