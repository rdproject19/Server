package httpserver.conversations;

import httpserver.Conversation;
import httpserver.DatabaseHandler;
import org.bson.Document;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConversationCreateServlet extends HttpServlet {

    DatabaseHandler h;

    public ConversationCreateServlet(DatabaseHandler handler) {
        h = handler;
    }

    /**
     * Create a new conversation
     * URL: /conversations/create
     *
     * Parameters should be:
     * - members (list of members for the group, separated by semicolons ';') [string]
     * - isgroup (whether this conversation is a group or not) [boolean]
     *
     * Will also enqueue an update for all members
     *
     * On success:
     *  Returns no text. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 400 (BAD REQUEST) status code. This likely means that the amount of members was smaller than two, or that the amount of members did not match the given conversation type.
     * OR
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that one of the provided members did not exist.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String[] members = req.getParameter("members").split(";");

        boolean group = Boolean.parseBoolean(req.getParameter("isgroup"));
        Document conversation = null;

        if (group) {
            if (members.length < 2) {
                res.setStatus(HttpStatus.BAD_REQUEST_400);
            } else {
                conversation = h.createNewConversation(members, true);
            }
        } else {
            if (members.length != 2) {
                res.setStatus(HttpStatus.BAD_REQUEST_400);
            } else {
                conversation = h.createNewConversation(members, false);
            }
        }

        if (conversation != null) {
            h.enqueueUserConversationUpdates(members, Conversation.fromDocument(conversation));
            res.setStatus(HttpStatus.OK_200);
        } else {
            res.setStatus(HttpStatus.GONE_410);
        }
    }
}
