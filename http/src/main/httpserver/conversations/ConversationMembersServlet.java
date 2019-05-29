package httpserver.conversations;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationMembersServlet extends HttpServlet {

    DatabaseHandler h;

    public ConversationMembersServlet(DatabaseHandler handler) {
        h = handler;
    }

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
