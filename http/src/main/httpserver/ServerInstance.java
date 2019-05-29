package httpserver;

import httpserver.conversations.ConversationCreateServlet;
import httpserver.user.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class ServerInstance {

    Server server;

    DatabaseHandler handler;

    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        handler = new DatabaseHandler();

        ServletContextHandler uctx = new ServletContextHandler(server, "/");

        activateServlets(uctx);

        server.start();
    }

    private void activateServlets(ServletContextHandler userContext) {
        ServletHolder nus = new ServletHolder(new NewUserServlet(handler));
        ServletHolder uces = new ServletHolder(new UserContactsEditServlet(handler));
        ServletHolder ucs = new ServletHolder(new UserContactsServlet(handler));
        ServletHolder ues = new ServletHolder(new UserEditServlet(handler));
        ServletHolder ugis = new ServletHolder(new UserGetImageServlet(handler));
        ServletHolder uls = new ServletHolder(new UserLoginServlet(handler));
        ServletHolder uuis = new ServletHolder(new UserUploadImageServlet());

        userContext.addServlet(nus, "/user/new");
        userContext.addServlet(ues, "/user/edit");
        userContext.addServlet(ugis, "/user/getimage");
        userContext.addServlet(uuis, "/user/image");
        userContext.addServlet(uls, "/user/login");

        userContext.addServlet(uces, "/user/contacts/edit");
        userContext.addServlet(ucs, "/user/contacts");

        ServletHolder ccs = new ServletHolder(new ConversationCreateServlet(handler));

        userContext.addServlet(ccs, "/conversations/new");
    }

}
