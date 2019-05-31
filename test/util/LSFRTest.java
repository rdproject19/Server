package util;

import com.google.common.hash.Hashing;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class LSFRTest {

    @Test
    public void testShift() {
        final String hash = "d74ae9d6ffc8e695699240e505965a74d523dfa992bab535340b57888a33134cb42b1069a2baa6a3c16298a07b0a385161110dcdc3b29f98078f9216e11b3993";

        LSFR l = new LSFR(hash);
        System.out.print(l.shift());
    }
}