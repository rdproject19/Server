package httpserver.user;

import com.google.common.hash.Hashing;
import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

public class UserCreateServlet extends HttpServlet {

    private DatabaseHandler h;

    public UserCreateServlet(DatabaseHandler h) {
        this.h = h;
    }

    /**
     * Create a new user
     * URL: /user/new
     *
     * Parameters should be:
     * - uname (uid for the new user) [string]
     * - pwd (password for the new user) [string]
     * - fullname (the full name (nickname) for the new user) [string]
     * - hasimage (whether or not a profile image is supplied) [boolean]
     * - (OPTIONAL) image (the filename of the image with extension) [string]
     *
     * On success:
     *  Returns no text. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 409 (CONFLICT) status code. This likely means that a user with the given username already exists
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String uname = req.getParameter("uname");
        String password = req.getParameter("pwd");
        String nick = req.getParameter("fullname");
        String token = Hashing.sha512().hashString(uname + password, Charset.defaultCharset()).toString();
        String image = "user_default";
        if (Boolean.parseBoolean(req.getParameter("hasimage"))) {
            image = req.getParameter("image");
        }

        int creationResult = h.createNewUser(uname, password, nick, token, image);

        res.setStatus(creationResult);
    }
}
