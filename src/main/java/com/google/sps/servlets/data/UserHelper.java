package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.servlets.AuthorizationServlet;
import javax.servlet.http.HttpServletRequest;
import com.google.sps.data.Type;

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

    if (request.getUserPrincipal().getName() == null) {
      return false;
    }

    for (Entity entity : results.asIterable()) {
      final String currentEmail = request.getUserPrincipal().getName();
      String emailStored = (String) entity.getProperty("Email");
      Long userType = (Long) entity.getProperty("Type");

      // Checking to see if user has an account in the entity
      if (userType == 4) {
      } else if (emailStored == null) {
      } else if (currentEmail.equals(emailStored)) {
        return true;
      }
    }
    return false;
  }

  public com.google.sps.data.Type getUserType(HttpServletRequest request) {
    com.google.sps.data.Type type = null;

    if (doesUserEmailExist(request) == true) {
      type = AuthorizationServlet.processType(request);
    }
    System.out.println(type);
    return type;
  }
}