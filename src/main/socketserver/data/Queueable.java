package socketserver.data;

import org.bson.Document;

/**
 * Simple interface for queueables
 */
public interface Queueable {
    Document toDocument();
}

