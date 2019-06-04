package socketserver.exceptions;

public class UserNotFoundException extends MessageHandleException {
    private String uid;
    public UserNotFoundException(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "No user with user ID: "+ this.uid +" exists";
    }
}
