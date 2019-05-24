package httpserver.user;

import com.google.common.hash.Hashing;
import httpserver.DatabaseHandler;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class NewUserServlet extends HttpServlet {

    DatabaseHandler h;

    public NewUserServlet(DatabaseHandler h) {
        this.h = h;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        if (!h.isActive()) return;
        res.setStatus(HttpStatus.OK_200);
        return;

        /*
        String uname = req.getParameter("uname");
        String pass = req.getParameter("password");
        String nickname = req.getParameter("nickname");
        String timestamp = req.getParameter("timestamp");

        String rawtoken = uname + pass + nickname + timestamp;

        String hashtoken = Hashing.sha512()
                .hashString(rawtoken, StandardCharsets.UTF_8)
                .toString();

        h.createNewUser(uname, pass, nickname, hashtoken);

        res.setStatus(HttpStatus.OK_200);
        */

    }
}
