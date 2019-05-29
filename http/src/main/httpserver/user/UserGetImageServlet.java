package httpserver.user;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;

public class UserGetImageServlet extends HttpServlet {

    private static final String PATH = "static/userimages/";
    private static final String EXTENSION = ".jpg";

    DatabaseHandler h;

    public UserGetImageServlet(DatabaseHandler handler) {
        h = handler;
    }

    /**
     * Get a users profile image
     * URL: /user/getimage
     *
     * Parameters should be:
     * - id (user id for whom the image should be fetched, without extension) [string]
     *
     * On success:
     *  Returns the image encoded in base64. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 500 (INTERNAL SERVER ERROR) status code.
     * OR
     *  Returns no text. Returns a 404 (NOT FOUND) status code. This means that the given image was not found.
     * OR
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the give user was not found.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String uid = req.getParameter("id");

        String id = h.getUserImage(uid);
        if (id.isEmpty()) {
            res.setStatus(HttpStatus.GONE_410);
            return;
        }

        File file = new File(PATH + id + EXTENSION);

        try {
            FileInputStream fs = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fs.read(data);

            String encoded = Base64.getEncoder().encodeToString(data);
            res.getWriter().print(encoded);
            res.setStatus(HttpStatus.OK_200);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            res.setStatus(HttpStatus.NOT_FOUND_404);
        } catch (IOException ex) {
            ex.printStackTrace();
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

}
