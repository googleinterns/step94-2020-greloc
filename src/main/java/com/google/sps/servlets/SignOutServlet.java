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
    name = "SignOutAPI",
    description = "SignOutAPI:Logout with UserService",
    urlPatterns = "/signoutapi"
)
public class SignOutServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    String thisUrl = req.getRequestURI();
    resp.setContentType("text/html");
  System.out.println("ENTERED THE SIGN OUT SERVER");
    if (userService.isUserLoggedIn()) {
      resp.sendRedirect(userService.createLogoutURL(thisUrl));
      System.out.println("USER STATUS: " + userService.isUserLoggedIn());
    } else {
      System.out.println("USER IS LOGGED OUT");
    }
  }
}
   
   
   
   
   
   
   
