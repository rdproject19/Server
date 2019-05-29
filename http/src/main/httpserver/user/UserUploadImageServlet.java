package httpserver.user;

import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Base64;

public class UserUploadImageServlet extends HttpServlet {

    private static final String PATH = "/static/userimages/";
    private static final String EXTENSION = ".jpg";

    /**
     * Upload a users profile image
     * URL: /user/image
     *
     * Parameters should be:
     * - id (id/filename for the image, without extension) [string]
     * - data (the image, encoded in base64) [string]
     *
     * On success:
     *  Returns no text. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 500 (INTERNAL SERVER ERROR) status code.
     */
    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse res) {
        String id = req.getParameter("id");
        String imgdata = req.getParameter("data");
        byte[] rawdata = Base64.getDecoder().decode(imgdata);

        try {
            OutputStream stream = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + PATH + id + EXTENSION);
            stream.write(rawdata);
            stream.close();
            stream.flush();
            res.setStatus(HttpStatus.OK_200);
        } catch (IOException ex) {
            ex.printStackTrace();
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

}
