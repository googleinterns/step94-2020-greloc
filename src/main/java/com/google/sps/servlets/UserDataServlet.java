import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.User;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userData")
public class UserDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    UserService userService = UserServiceFactory.getUserService();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    try {
      if (userService.isUserLoggedIn()) {
        Query query = new Query("UserData");
        PreparedQuery results = datastore.prepare(query);

        for (Entity entity : results.asIterable()) {
          String email = (String) entity.getProperty("Email");
          String userIDStored = (String) entity.getProperty("userID");
          long userType = (long) entity.getProperty("Type");
          String userID = userService.getCurrentUser().getUserId();

          if (userID.equals(userIDStored)) {
            User user = new User(userType, email, userIDStored);
            Gson gson = new Gson();
            response.setContentType("application/json;");
            response.getWriter().println(gson.toJson(user));
          }
        }
      } else {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
