package data;

public class CacheObject {

    private boolean valid = true;

    /**
     * Invalidates this cache object. Flags it so that when the data is requested, it is again fetched from the database
     */
    public void invalidate() {
        valid = false;
    }

    /**
     * Checks if this cache object is valid
     * @return Whether or not this cache object is valid
     */
    public boolean isValid() {
        return valid;
    }
}
