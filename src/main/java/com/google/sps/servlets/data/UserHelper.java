package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.http.HttpServletRequest;

public final class UserHelper {

  private DatastoreService datastore;

  public UserHelper(DatastoreService datastore) {
    this.datastore = datastore;
  }

  public Boolean doesUserEmailExist(HttpServletRequest request) {
    UserService userService = UserServiceFactory.getUserService();

    // Creating a Query to search through the datastore
    Query query = new Query("testData");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      final String currentEmail = request.getUserPrincipal().getName();
      String emailStored = (String) entity.getProperty("Email");
      String userType = (String) entity.getProperty("Type");

      System.out.println("Current Email: " + currentEmail);
      System.out.println("Email Stored: " + emailStored);
      System.out.println("UserType: " + userType);
      System.out.println();

      // Checking to see if user has an account in the entity
      if (userType == null) {
        System.out.println("This user does not have a userType");
        return false;
      } else if (currentEmail == null) {
        System.out.println("Current email is null");
        return false;
      } else if (emailStored == null) {
        System.out.println("Email in database is null");
        return false;
      } else if (!currentEmail.equals(emailStored)) {
        System.out.println("Cannot find that email in the datastore");
        return false;
      } else if (currentEmail.equals(emailStored) && userType != null) {
        System.out.println("Email thats trying to login: " + currentEmail);
        System.out.println("Email thats trying to match from entity: " + emailStored);
        System.out.println("Its a match!");
        return true;
      } else {
        System.out.println("Something went wrong");
      }
    }
    return false;
  }
}
