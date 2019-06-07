package httpserver.common;

import httpserver.DatabaseHandler;
import org.bson.Document;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

/**
 * Get an image
 * URL: /images
 * Use a PUT request to upload, or a GET request to download.
 */
public class ImageServlet extends HttpServlet {

    private static final String USERPATH = "static/userimages/";
    private static final String CONVERSATIONPATH = "static/convimages/";
    private static final String EXTENSION = ".jpg";

    DatabaseHandler h;

    public ImageServlet(DatabaseHandler handler) {
        h = handler;
    }

    /**
     * Parameters should be:
     * For Upload:
     * - type (what image should be retrieved. allowed values are 'group', or 'user') [string]
     * - id (user/conversation id for whom the image should be fetched, without extension) [string]
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
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String id = req.getParameter("id");
        String type = req.getParameter("type");

        String path, imgid;
        if (type.equals("g")) {
            path = CONVERSATIONPATH;
            imgid = h.getConversationImage(id);
        } else {
            path = USERPATH;
            imgid = h.getUserField(id, "image");
        }

        if (imgid.isEmpty()) {
            res.setStatus(HttpStatus.GONE_410);
            return;
        }

        File file = new File(path + imgid + EXTENSION);

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

    /**
     * Parameters should be:
     * - type (what image should be retrieved. allowed values are 'group', or 'user') [string]
     * - id (id for the group or user) [string]
     * - socketserver.data (the image, encoded in base64) [string]
     *
     * On success:
     *  Returns the uploaded image's id. Returns a 200 (OK) status code.
     * On failure:
     *  Returns no text. Returns a 500 (INTERNAL SERVER ERROR) status code.
     * OR
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given user/group id didn't exist
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        String type = req.getParameter("type");
        String id = req.getParameter("id");
        String imgdata = req.getParameter("socketserver.data");
        byte[] rawdata = Base64.getDecoder().decode(imgdata);

        String imageid = UUID.randomUUID().toString() + "_" + id; //Adding id guarantees uniqueness. Using random UUID ensures static sweeping is not possible.

        String path;
        int c;
        if (type.equals("g")) {
            path = CONVERSATIONPATH;
            c = h.editConversation(id, new Document().append("image", imageid));
        } else {
            path = USERPATH;
            c = h.updateUser(id, new Document().append("image", imageid)) ? 200 : 410;
        }

        if (c != 200) {
            res.setStatus(c);
            return;
        }

        try {
            OutputStream stream = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + path + imageid + EXTENSION);
            stream.write(rawdata);
            stream.close();
            stream.flush();
            res.getWriter().print(imageid);
            res.setStatus(HttpStatus.OK_200);
        } catch (IOException ex) {
            ex.printStackTrace();
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

}
