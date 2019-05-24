package java.user;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewUserServlet extends HttpServlet {

    protected void doPost(HttpServletResponse res, HttpServletRequest req) {
        String uname = req.getParameter("uname");
        String pass = req.getParameter("password");
        String nickname = req.getParameter("nickname");

        String token = new StringBuilder(uname).append(pass).append(nickname).append("dd");

    }
}
