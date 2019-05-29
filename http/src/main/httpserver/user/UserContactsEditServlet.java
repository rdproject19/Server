package httpserver.user;

import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserContactsEditServlet extends HttpServlet {

    private DatabaseHandler h;

    public UserContactsEditServlet(DatabaseHandler h) {
        this.h = h;
    }

    /**
     * Edits a users contact list
     * URL: /user/contacts/edit
     * Use a PUT request to add, a DELETE request to remove
     * ATTENTION: Due to weird quirks, the PUT request should use body (x-www-form-urlencoded) parameters, but the DELETE request should use url parameters
     *
     * Parameters should be:
     * - uname (uid for the user whose contacts list is to be updated) [string]
     * - contact (uid for the user to be added or removed from the contact list) [string]
     *
     * On success:
     *  Returns no text. Returns a 200 (OK) status code.
     * OR
     *  Returns no text. Returns a 304 (NOT MODIFIED) status code. This likely means that the given contact is already on the users contact list
     * On failure:
     *  Returns no text. Returns a 410 (GONE) status code. This likely means that the given user (target) does not exist.
     *  OR
     *  Returns no text. Returns a 202 (ACCEPTED) status code. This likely means that the given contact id was not on the contact list (in case of removal) or that no user associated with the given contact id exists.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)  {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        DatabaseHandler.EditAction act = DatabaseHandler.EditAction.ADD;

        String uid = req.getParameter("uname");
        String contact = req.getParameter("contact");

        res.setStatus(h.editContacts(uid, contact, act));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);

        DatabaseHandler.EditAction act = DatabaseHandler.EditAction.DELETE;

        String uid = req.getParameter("uname");
        String contact = req.getParameter("contact");

        res.setStatus(h.editContacts(uid, contact, act));
    }
}
