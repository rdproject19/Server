package httpserver.conversations;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ConversationMembersServlet extends HttpServlet {

    DatabaseHandler h;

    public ConversationMembersServlet(DatabaseHandler handler) {
        h = handler;
    }

    /**
     * Gets a conversations members
     * URL: /conversations/members
     *
     * Parameters should be:
     * - gid (the group id) [string]
     *
     * On success:
     *  Returns a list of the conversations members, separated by semicolon ';'. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given group id does not exist
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        List<String> members = h.getConversationMembers(req.getParameter("gid"));
        if (members != null) {
            String membersString = String.join(";", members);
            res.getWriter().print(membersString);
            res.setStatus(HttpStatus.OK_200);
        } else {
            res.setStatus(HttpStatus.GONE_410);
        }
    }
}
