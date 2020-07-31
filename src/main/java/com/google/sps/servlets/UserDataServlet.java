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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

@WebServlet("/userData")
public class UserDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    UserService userService = UserServiceFactory.getUserService();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    String userId = userService.getCurrentUser().getUserId();
    try {
      if (userService.isUserLoggedIn()) {
          Filter userFilter = new FilterPredicate("userID", FilterOperator.EQUAL,userId);
          Query query = new Query("UserData").setFilter(userFilter);
          PreparedQuery resultsPR = datastore.prepare(query);
          Entity results = resultsPR.asSingleEntity();

          String email = (String) results.getProperty("Email");
          String userIDStored = (String) results.getProperty("userID");
          long userType = (long) results.getProperty("Type");

          User user = new User(userType, email, userIDStored);
          Gson gson = new Gson();
          response.setContentType("application/json;");
          response.getWriter().println(gson.toJson(user));
      } else {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}