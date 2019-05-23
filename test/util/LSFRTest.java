package util;

import com.google.common.hash.Hashing;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class LSFRTest {

    @Test
    public void testShift() {
        final String hash = Hashing.sha512()
                .hashString("gewgwegwwgegwghwewegwwherhjerhjer", StandardCharsets.UTF_8)
                .toString();

        LSFR l = new LSFR(hash);
        for (int i = 0; i < 10; i++) {
            System.out.println(l.shift());
        }
    }
}