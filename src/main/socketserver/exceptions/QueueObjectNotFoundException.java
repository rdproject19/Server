package socketserver.exceptions;

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
