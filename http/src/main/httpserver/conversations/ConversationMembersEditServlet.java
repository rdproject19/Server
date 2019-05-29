package httpserver.conversations;

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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String gid = req.getParameter("gid");
        List<String> newmembers = Arrays.stream(req.getParameter("members").split(";"))
                .collect(Collectors.toList());

        if (h.updateConversationMembers(gid, newmembers, DatabaseHandler.EditAction.ADD)) {
            res.setStatus(HttpStatus.OK_200);
        } else {
            res.setStatus(HttpStatus.GONE_410);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String gid = req.getParameter("gid");
        List<String> pastmembers = Arrays.stream(req.getParameter("members").split(";"))
                .collect(Collectors.toList());

        if (h.updateConversationMembers(gid, pastmembers, DatabaseHandler.EditAction.DELETE)) {
            res.setStatus(HttpStatus.OK_200);
        } else {
            res.setStatus(HttpStatus.GONE_410);
        }
    }
}
