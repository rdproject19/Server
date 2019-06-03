package socketserver;

import com.google.common.hash.Hashing;
import org.xml.sax.SAXException;
import socketserver.server.SocketServer;
import socketserver.util.Configuration;
import socketserver.util.LSFR;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
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

        SocketServer s = null;
        try {
            s = new SocketServer(new InetSocketAddress(host, port), new Configuration());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        s.run();

        System.out.print("a");
    }
}
