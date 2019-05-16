import server.SocketServer;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) {
        final String host = "localhost";
        final int port = 7070;

        SocketServer s = new SocketServer(new InetSocketAddress(host, port));
        s.run();
    }
}
