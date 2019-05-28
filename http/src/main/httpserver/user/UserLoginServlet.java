package httpserver.user;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginServlet extends HttpServlet {

    private DatabaseHandler h;

    public UserLoginServlet(DatabaseHandler h) {
        this.h = h;
    }

    /**
     * Checks user credentials
     * URL: /user/login
     *
     * Parameters should be:
     * - uname (The user name) [string]
     * - pass (The password) [string]
     *
     * On success:
     *  Returns no text. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 401 (UNAUTHORIZED) status code. This likely means that that either username or password were incorrect (or both).
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String u = req.getParameter("uname");
        String p = req.getParameter("pass");

        if (h.checkUserCredentials(u, p)) {
            res.setStatus(HttpStatus.OK_200);
        } else {
            res.setStatus(HttpStatus.UNAUTHORIZED_401);
        }
    }
}
