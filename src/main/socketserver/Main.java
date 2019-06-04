package socketserver;

import com.google.common.hash.Hashing;
import org.xml.sax.SAXException;
import socketserver.server.SocketServer;
import socketserver.util.Configuration;
import socketserver.util.LSFR;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        final String hash = Hashing.sha512()
                .hashString("gewgwegwwgegwghwewegwwherhjerhjer", StandardCharsets.UTF_8)
                .toString();

        StringBuilder sb = new StringBuilder(hash).reverse();
        final String rehash = Hashing.sha512()
                .hashString(sb.toString(), StandardCharsets.UTF_8)
                .toString();

        LSFR l = new LSFR(hash);

        SocketServer s = null;
        try {
            Configuration c = new Configuration();
            s = new SocketServer(new InetSocketAddress(c.getServerHost(), c.getServerPort()), c);
            s.run();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

            SocketServer finalS = s;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down...");
                    finalS.stop();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }));
    }
}
