package httpserver;

import httpserver.user.NewUserServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServerInstance {

    Server server;

    DatabaseHandler handler;

    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        handler = new DatabaseHandler();

        ServletContextHandler ctx = new ServletContextHandler(server, "/");

        ctx.addServlet(createNewUserServlet(), "/newuser");

        server.start();
    }

    public ServletHolder createNewUserServlet() {
        ServletHolder h = new ServletHolder(new NewUserServlet(handler));
        return h;
    }

}
