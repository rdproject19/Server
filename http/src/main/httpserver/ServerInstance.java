package httpserver;

import httpserver.common.ImageServlet;
import httpserver.conversations.*;
import httpserver.user.*;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;

public class ServerInstance {

    Server server;

    DatabaseHandler handler;

    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        handler = new DatabaseHandler();

        createStaticFolders();

        ServletContextHandler uctx = new ServletContextHandler(server, "/");
        activateServlets(uctx);

        server.start();
        System.out.println("HTTP - Server started");
    }

    private void activateServlets(ServletContextHandler ctx) {
        //User context
        ServletHolder nus = new ServletHolder(new UserCreateServlet(handler));
        ServletHolder uces = new ServletHolder(new UserContactsEditServlet(handler));
        ServletHolder ucs = new ServletHolder(new UserContactsServlet(handler));
        ServletHolder ues = new ServletHolder(new UserEditServlet(handler));
        ServletHolder uls = new ServletHolder(new UserLoginServlet(handler));
        ServletHolder ugn = new ServletHolder(new UserGetNameServlet(handler));

        ctx.addServlet(nus, "/user/new");
        ctx.addServlet(ues, "/user/edit");
        ctx.addServlet(uls, "/user/login");
        ctx.addServlet(uces, "/user/contacts/edit");
        ctx.addServlet(ucs, "/user/contacts");
        ctx.addServlet(ugn, "/user/name");

        //Conversation context
        ServletHolder ccs = new ServletHolder(new ConversationCreateServlet(handler));
        ServletHolder ces = new ServletHolder(new ConversationEditServlet(handler));
        ServletHolder cmes = new ServletHolder(new ConversationMembersEditServlet(handler));
        ServletHolder cms = new ServletHolder(new ConversationMembersServlet(handler));
        ServletHolder cs = new ServletHolder(new ConversationServlet(handler));

        ctx.addServlet(ccs, "/conversations/new");
        ctx.addServlet(ces, "/conversations/edit");
        ctx.addServlet(cmes, "/conversations/members/edit");
        ctx.addServlet(cms, "/conversations/members");
        ctx.addServlet(cs, "/conversations");

        //Common context
        ServletHolder ugis = new ServletHolder(new ImageServlet(handler));

        ctx.addServlet(ugis, "/images");
    }

    private void createStaticFolders() {
        File f = new File("static");
        if (!f.exists()) f.mkdir();
        File f1 = new File("static/userimages");
        if (!f1.exists()) f1.mkdir();
        File f2 = new File("static/convimages");
        if (!f2.exists()) f2.mkdir();
    }
}
