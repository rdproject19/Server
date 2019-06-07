package socketserver;

import org.xml.sax.SAXException;
import socketserver.server.SocketServer;
import socketserver.util.Configuration;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) {
        SocketServer s = null;
        try {
            Configuration c = new Configuration();
            s = new SocketServer(new InetSocketAddress(c.getServerHost(), c.getServerPort()), c);
            s.run();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        //To unbind the address on shutdown
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
