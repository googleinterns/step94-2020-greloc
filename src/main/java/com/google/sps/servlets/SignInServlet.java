package com.example.appengine.users;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(
    name = "SignInAPI",
    description = "SignInAPI: Login with UserService",
    urlPatterns = "/signinapi"
)
public class SignInServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    String thisUrl = req.getRequestURI();

    resp.setContentType("text/html");
    if (req.getUserPrincipal() != null) {
      System.out.println("USER IS LOGGED IN");
      resp.sendRedirect("/authorization");
    } if(!userService.isUserLoggedIn()) {
      resp.sendRedirect(userService.createLoginURL(thisUrl));
    }
  }
}