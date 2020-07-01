// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.PrintWriter;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.util.ArrayList;
import java.util.List;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/authentication")
public class AuthenticationServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    PrintWriter out = response.getWriter();

    UserService userService = UserServiceFactory.getUserService();
    
    // Actions after users are authorized
    // if (userService.isUserLoggedIn() /*&& userService.getCurrentUser().getAuthDomain() == "google.com" */) {
    //   response.sendRedirect("/dashboard/index.html");
    //   String urlToRedirectToAfterUserLogsOut = "/index.html";
    //   String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

    //   // Actions when users are not authorized
    // } else {
    //   System.out.println("NOT LOGGED IN");
    //   String loginUrl = userService.createLoginURL("/authentication");
    //   out.println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");

    // }

  Query query = new Query("User Data");
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();   
  PreparedQuery results = datastore.prepare(query);

  final String emailLogin = request.getParameter("email");
  final String pswLogin = request.getParameter("pswd");

  for(Entity entity: results.asIterable()) {
    String userEmail = (String) entity.getProperty("Email");
    String userPass = (String) entity.getProperty("Password");
    String userType = (String) entity.getProperty("type");

    if(emailLogin.equals(userEmail) && pswLogin.equals(userPass)) {
      System.out.println(emailLogin);
      System.out.println(userEmail);
      System.out.println("Its a match!");
      response.sendRedirect("/dashboard/index.html");
    } else {
      String loginUrl = userService.createLoginURL("/registration");
      System.out.println("Its not a match");
      System.out.println(emailLogin);
      System.out.println(userEmail);
      response.sendRedirect("/registration.html");
      // out.println("<p> Either username or password is incorrect. Try again or create an account"
      // <a href>\+registerUrl + "\"> here </a>. </p>");
    }
  }

  }

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
     
     //Creating a user from the registration page

    final String emailEntered = request.getParameter("email");
    final String pswEntered = request.getParameter("psw");
    final String typeEntered = request.getParameter("type");

    //Creating an UserEntity
    Entity taskEntity = new Entity("User Data");
    taskEntity.setProperty("Email", emailEntered);
    taskEntity.setProperty("Password", pswEntered);
    taskEntity.setProperty("Type", typeEntered);

  //Storing the Entity in datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    response.sendRedirect("/dashboard/index.html");
   }
}