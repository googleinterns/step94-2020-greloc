package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.http.HttpServletRequest;

/* This class verifies the user is located in the database */

public final class UserHelper {

  private DatastoreService datastore;

  public UserHelper(DatastoreService datastore) {
    this.datastore = datastore;
  }

  public Boolean doesUserEmailExist(HttpServletRequest request) {
    UserService userService = UserServiceFactory.getUserService();

    // Creating a Query to search through the datastore
    Query query = new Query("UserData");
    PreparedQuery results = datastore.prepare(query);

    if (request.getUserPrincipal().getName() == null) {
      return false;
    }

    for (Entity entity : results.asIterable()) {
      final String currentEmail = request.getUserPrincipal().getName();
      String emailStored = (String) entity.getProperty("Email");
      long userType = (long) entity.getProperty("Type");

      // Checking to see if user has an account in the entity
      if (userType == UserType.UNKNOWN.getValue()) {
      } else if (emailStored == null) {
      } else if (currentEmail.equals(emailStored)) {
        return true;
      }
    }
    return false;
  }
}
