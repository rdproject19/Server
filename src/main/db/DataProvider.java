package db;

import exceptions.UserNotFoundException;

/**
 * Manages data I/O.
 * I.E. Checks if requested items are cached, and if not, fetches them from the database.
 */
public class DataProvider {

    public CacheManager cache;

    public DataProvider() {
        this.cache = new CacheManager();
    }

    public UserCacheObject getUserProfile(String uid) {
        try {
            UserCacheObject cacheObject = cache.getUser(uid);
            return cacheObject;
        } catch (UserNotFoundException ex) {
            //Fetch from DB;
        }
        return null;
    }
}
