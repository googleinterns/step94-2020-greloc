import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

@WebServlet("/locations")
public class UserDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    UserService userService = UserServiceFactory.getUserService();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


      if(userService.isUserLoggedIn()) {
        Query query = new Query("UserData");
        PreparedQuery results = datastore.prepare(query);

        for(Entity entity : results.asIterable()) {
            String userID = (String) entity.getProperty("userID");
        }

        if(true) {
            System.out.println("HERE");
        }
      } else {
          System.out.println("throw an 404 403 error");
      }
  }

}