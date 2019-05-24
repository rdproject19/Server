package data;

import com.google.common.hash.Hashing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataProviderTest {

    DataProvider provider;

    @BeforeAll
    public void setup() {
        this.provider = new DataProvider();
        final String hash = Hashing.sha512()
                .hashString("gewgwegwwgegwghwewegwwherhjerhjer", StandardCharsets.UTF_8)
                .toString();
        provider.cache.addUser("koen", hash, null);
    }

    @Test
    public void testGetUserProfile() {
        UserCacheObject obj = provider.getUserProfile("koen");
        UserCacheObject obj2 = provider.getUserProfile("k");
        assertNotNull(obj);
        assertNull(obj2);
    }
}