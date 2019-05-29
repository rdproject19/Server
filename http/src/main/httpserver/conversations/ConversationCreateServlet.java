package httpserver.conversations;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

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
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String[] members = req.getParameter("members").split(";");

        boolean group = Boolean.parseBoolean(req.getParameter("isgroup"));
        if (group) {
            if (members.length <= 2) {
                res.setStatus(HttpStatus.BAD_REQUEST_400);
            } else {
                h.createNewConversation(members, true);
                res.setStatus(HttpStatus.OK_200);
            }
        } else {
            if (members.length != 2) {
                res.setStatus(HttpStatus.BAD_REQUEST_400);
            } else {
                h.createNewConversation(members, false);
                res.setStatus(HttpStatus.OK_200);
            }
        }
    }
}
