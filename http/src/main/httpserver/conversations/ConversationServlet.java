package httpserver.conversations;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConversationServlet extends HttpServlet {

    DatabaseHandler h;

    public ConversationServlet(DatabaseHandler handler) {
        this.h = handler;
    }

    /**
     * Gets a conversations details (title, description etc).
     * URL: /conversations
     *
     * Parameters should be:
     * - gid (the group id) [string]
     *
     * On success:
     *  Returns the details of the conversation as json (for details, see ConversationEditServlet). Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given group id does not exist.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String gid = req.getParameter("gid");

        String data = h.getConversationDetails(gid);
        if (data.isEmpty()) {
            res.setStatus(HttpStatus.GONE_410);
            return;
        }

        res.getWriter().print(data);
        res.setStatus(HttpStatus.OK_200);
    }
}
