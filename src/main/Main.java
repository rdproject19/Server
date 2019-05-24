import com.google.common.hash.Hashing;

import server.SocketServer;
import util.Configuration;
import util.LSFR;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        final String host = "0.0.0.0";
        final int port = 7070;

        final String hash = Hashing.sha512()
                .hashString("gewgwegwwgegwghwewegwwherhjerhjer", StandardCharsets.UTF_8)
                .toString();

        StringBuilder sb = new StringBuilder(hash).reverse();
        final String rehash = Hashing.sha512()
                .hashString(sb.toString(), StandardCharsets.UTF_8)
                .toString();

        LSFR l = new LSFR(hash);

        SocketServer s = new SocketServer(new InetSocketAddress(host, port), new Configuration());
        s.run();

        System.out.print("a");
    }
}
