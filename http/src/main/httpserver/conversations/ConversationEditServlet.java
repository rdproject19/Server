package httpserver.conversations;

import httpserver.DatabaseHandler;
import org.bson.Document;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class ConversationEditServlet extends HttpServlet {

    DatabaseHandler h;

    public ConversationEditServlet(DatabaseHandler handler) {
        h = handler;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String gid = req.getParameter("id");
        Document d = new Document();
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (param.equals("isgroup")) {
                res.setStatus(HttpStatus.BAD_REQUEST_400);
                return;
            }
            d.append(param, req.getParameter(param));
        }

        res.setStatus(h.editConversation(gid, d));
    }
}
