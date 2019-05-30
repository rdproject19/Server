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

    /**
     * Edits a conversation (group)
     * URL: /conversations/edit
     *
     * Parameters should be:
     * - Any parameters that need to be edited. (The same fields as when creating a new conversation)
     *   VALID FIELDS ARE:
     *   - title: the 'name'/'title' of the conversation.
     *   - description: the description of the conversation.
     *   To modify the group image, use the image servlet.
     *   EXCEPT: the parameter 'isgroup' is not accepted
     * - gid (The group id of the group to be edited) [string]
     *
     * On success:
     *  Returns no text. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 400 (BAD REQUEST) status code. This likely means that an illegal field was provided, or that the given conversation could not be modified, because it is not a group.
     * OR
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given group id does not exist
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String gid = req.getParameter("gid");
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
