package socketserver.exceptions;

/**
 * Thrown if a queued object does not exist in the queue database
 */
public class QueueObjectNotFoundException extends Exception {
    String id;
    public QueueObjectNotFoundException(String objectID) {
        this.id = objectID;
    }

    @Override
    public String toString() {
        return "Cache object " + id + " was defined in the users queue, but no such cache object exists";
    }
}
