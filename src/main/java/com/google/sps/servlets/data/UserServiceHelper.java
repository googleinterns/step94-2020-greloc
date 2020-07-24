package com.google.sps.data;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;  
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.sps.servlets.AuthorizationServlet; 

public class UserServiceHelper extends HttpServlet {

  interface Callback {
    //if test pass, put code you want to happen here
    void handleResponse(HttpServletResponse resp, HttpServletRequest req ,Type type);
  }

  public void authUser(Callback callback, HttpServletResponse resp, HttpServletRequest req) throws IOException {
    // 1. Check if user is logged in using UseService
    // 2. Check if dataStore has info 
    
    UserService userService = UserServiceFactory.getUserService();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserHelper userHelper = new UserHelper(datastore);
    
    Type type = userHelper.getUserType(req);

    if (userService.isUserLoggedIn()  && userHelper.doesUserEmailExist(req) == true ) {
      // pass whatever info you need into handleReseponse() method.
      callback.handleResponse(resp, req, type);
    } 
    if (!userService.isUserLoggedIn()) {
      //change the link variable to your specific redirect link
      resp.sendRedirect("/signinapi");
    }
  }
}