package httpserver.user;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserGetNameServlet extends HttpServlet {

    DatabaseHandler h;

    public UserGetNameServlet(DatabaseHandler handler) {
        h = handler;
    }

    /**
     * Gets a users full name
     * URL: /user/name
     *
     * Parameters should be:
     * - uname (uid for the user) [string]
     *
     * On success:
     *  Returns the users full name as set in the database. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given user id does not exist.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String id = req.getParameter("uname");

        String nm = h.getUserField(id, "fullname");

        if (nm.isEmpty()) {
            res.setStatus(HttpStatus.GONE_410);
        } else {
            res.getWriter().print(nm);
            res.setStatus(HttpStatus.OK_200);
        }
    }
}
