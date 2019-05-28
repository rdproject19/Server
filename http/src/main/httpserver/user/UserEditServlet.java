package httpserver.user;

import httpserver.DatabaseHandler;
import org.bson.Document;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class UserEditServlet extends HttpServlet {

    private DatabaseHandler h;

    public UserEditServlet(DatabaseHandler h) {
        this.h = h;
    }

    /**
     * Edits a users profile
     * URL: /user/edit
     *
     * Parameters should be:
     * - Any parameters that need to be edited. (The same fields as when creating a new user)
     *   EXCEPT: fields 'hasimage' and 'uname' are not accepted.
     *
     * On success:
     *  Returns no text. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given user (target) does not exist.
     * OR
     *  Returns no text. Returns a 400 (BAD REQUEST) status code. This likely means that an illegal field was provided.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        Document d = new Document();
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (param.equals("uname") || param.equals("hasimage")) {
                res.setStatus(HttpStatus.BAD_REQUEST_400);
                return;
            }
            d.append(param, req.getParameter(param));
        }

        if (h.updateUser((String) d.get("uname"), d)) {
            res.setStatus(HttpStatus.OK_200);
        } else {
            res.setStatus(HttpStatus.GONE_410);
        }
    }
}
