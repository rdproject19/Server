package socketserver.util;

import org.junit.jupiter.api.Test;

class LSFRTest {

    @Test
    public void testShift() {
        final String hash = "f759f3ef9aaf84d812a13f3aadb31ee882c3d9a8a79d9fcc8dd38eb1c5fc91232707e66822dbe43e893b67eac2468dcfd4283cf4537afdf5d90294f472308a42";

        LSFR l = new LSFR(hash, 0);
        System.out.print(l.shift());
    }
}