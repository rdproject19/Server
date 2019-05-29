package httpserver.conversations;

import httpserver.DatabaseHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConversationCreateServlet extends HttpServlet {

    DatabaseHandler h;

    public ConversationCreateServlet(DatabaseHandler handler) {
        h = handler;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
    }
}
