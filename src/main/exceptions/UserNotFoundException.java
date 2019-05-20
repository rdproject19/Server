package exceptions;

public class UserNotFoundException extends Exception {
    private String uid;
    public UserNotFoundException(String uid) {
        this.uid = uid;
    }

    public String toString() {
        return "No user with user ID: "+ this.uid +" exists";
    }
}
