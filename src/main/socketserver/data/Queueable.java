package socketserver.data;

import org.bson.Document;

public interface Queueable {
    Document toDocument();
}
