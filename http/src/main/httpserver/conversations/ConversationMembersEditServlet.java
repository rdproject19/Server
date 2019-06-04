package httpserver.conversations;

import httpserver.Conversation;
import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationMembersEditServlet extends HttpServlet {

    DatabaseHandler h;

    public ConversationMembersEditServlet(DatabaseHandler handler) {
        h = handler;
    }

    /**
     * Edits a conversations members
     * URL: /conversations/members/edit
     * Use a PUT request to add, a DELETE request to remove
     * ATTENTION: Due to weird quirks, the PUT request should use body (x-www-form-urlencoded) parameters, but the DELETE request should use url parameters
     *
     * Parameters should be:
     * - gid (the group id for the group) [string]
     * - members (a list of members to be added or removed, separated by semicolons ';') [string]
     *
     * Also enqueues an update for the new members
     *
     * On success:
     *  Returns no text. Returns a 200 (OK) status code.
     * OR
     *  Returns no text. Returns a 304 (NOT MODIFIED) status code. This likely means that the members list did not change because of the addition or removal of members
     * On failure:
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given group id does not exist.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String gid = req.getParameter("gid");
        List<String> newmembers = Arrays.stream(req.getParameter("members").split(";"))
                .collect(Collectors.toList());

        int c = h.updateConversationMembers(gid, newmembers, DatabaseHandler.EditAction.ADD);

        if (c == 200) {
            Conversation conversation = Conversation.fromDocument(h.getConversation(gid));
            h.enqueueUserConversationUpdates((String[]) newmembers.toArray(), conversation);
        }

        res.setStatus(c);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String gid = req.getParameter("gid");
        List<String> pastmembers = Arrays.stream(req.getParameter("members").split(";"))
                .collect(Collectors.toList());

        int c = h.updateConversationMembers(gid, pastmembers, DatabaseHandler.EditAction.DELETE);
        res.setStatus(c);
    }
}
