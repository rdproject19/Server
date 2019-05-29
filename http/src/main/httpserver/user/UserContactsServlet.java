package httpserver.user;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserContactsServlet extends HttpServlet {

    private DatabaseHandler h;

    public UserContactsServlet(DatabaseHandler h) {
        this.h = h;
    }

    /**
     * Gets a users contacts list
     * URL: /user/contacts
     *
     * Parameters should be:
     * - uname (uid for the user whose contacts list is to be retrieved) [string]
     *
     * On success:
     *  Returns a list of the users contacts, separated by commas (,). Returns a 200 (OK) status code.
     * OR
     *  Returns no text. Returns a 204 (NO CONTENT) status code. This likely means that the given user has no contacts
     * On failure:
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given user does not exist.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String u = req.getParameter("uname");

        List<String> contacts = h.getContacts(u);

        if (contacts.isEmpty()) {
            res.setStatus(HttpStatus.NO_CONTENT_204);
            return;
        }

        res.getWriter().print(String.join(",", contacts));
        res.setStatus(HttpStatus.OK_200);
    }
}
