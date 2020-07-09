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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* This class handles authorization of the user*/

@WebServlet("/authorization")
public class AuthorizationServlet extends HttpServlet {

  public enum Type {
    RENTER,
    HOST,
    BOTH,
    UNKNOWN;
  }

  @Override
<<<<<<< HEAD
 public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
   response.setContentType("text/html;");
   PrintWriter out = response.getWriter();
 
   UserService userService = UserServiceFactory.getUserService();
  
   //Creating a Query to search through the datastore
   Query query = new Query("User Data");
   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();  
   PreparedQuery results = datastore.prepare(query);
 
   final String emailLogin = request.getParameter("email");
   final String pswLogin = request.getParameter("pswd");
  
   //Iterating through the entity
   for(Entity entity: results.asIterable()) {
     String userEmail = (String) entity.getProperty("Email");
     String userPass = (String) entity.getProperty("Password");
     String userType = (String) entity.getProperty("Type");
 
     //Checking to see if user has an account in the entity
     if(emailLogin.equals(userEmail) && pswLogin.equals(userPass)) {
       System.out.println("Email thats trying to login: " + emailLogin);
       System.out.println("Email thats trying to match from entity: " + userEmail);
       System.out.println("Its a match!");
       System.out.println("BREAK");
       response.sendRedirect("/dashboard/index.html"); 
       return;
     }
   }
   response.sendRedirect("/registration.html");
    
 }
 
 //A helper method that helps identifies the UserType
 private Type processType(HttpServletRequest request) {
   Type type = null;
   String userType = request.getParameter("type");
   System.out.println(userType);
   if (userType.equals("renter")) {
       type = Type.RENTER;
     } else if (userType.equals("host")) {
       type = Type.HOST;
     } else if (userType.equals("both")) {
       type = Type.BOTH;
     } else {
       type = Type.UNKNOWN;
     }
     System.out.println(type);
   return type;
 }
 
 // // Helper method that makes sure the password matches
 // private void passwordMatch() {
 //   String pass = request.getParameter("psw");
 //   String repeatPass = request.getParameter("psw-repeat");
 
 //   if(pass.equals(repeatPass)) {
 //     return pass;
 //   } else {
     
 //   }
 // }
 
 public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
   //Creating a user from the registration page
   final String emailEntered = request.getParameter("email");
   final String pswEntered = request.getParameter("psw");
   final String typeEntered = processType(request).toString();
 
   //Creating an UserEntity
   Entity taskEntity = new Entity("User Data");
   taskEntity.setProperty("Email", emailEntered);
   taskEntity.setProperty("Password", pswEntered);
   taskEntity.setProperty("Type", typeEntered);
 
   //Storing the Entity in datastore
   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
   datastore.put(taskEntity);
 
   //After user registers, send to the dashboard page
   response.sendRedirect("/dashboard/index.html");
 }
 
=======
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    PrintWriter out = response.getWriter();

    UserService userService = UserServiceFactory.getUserService();

    // Creating a Query to search through the datastore
    Query query = new Query("User Data");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    final String emailLogin = request.getParameter("email");
    final String pswLogin = request.getParameter("pswd");

    // Iterating through the entity
    for (Entity entity : results.asIterable()) {
      String userEmail = (String) entity.getProperty("Email");
      String userPass = (String) entity.getProperty("Password");
      String userType = (String) entity.getProperty("type");

      // Checking to see if user has an account in the entity
      if (emailLogin.equals(userEmail) && pswLogin.equals(userPass)) {
        System.out.println("Email thats trying to login: " + emailLogin);
        System.out.println("Email thats trying to match from entity: " + userEmail);
        System.out.println("Its a match!");
        System.out.println("BREAK");
        response.sendRedirect("/dashboard/index.html");
        return;
      }
    }
    response.sendRedirect("/registration.html");
  }

  // A helper method that helps identifies the UserType
  private Type processType(HttpServletRequest request) {
    Type type = null;
    String userType = request.getParameter("type");
    System.out.println(userType);
    if (userType.equals("renter")) {
      type = Type.RENTER;
    } else if (userType.equals("host")) {
      type = Type.HOST;
    } else if (userType.equals("both")) {
      type = Type.BOTH;
    } else {
      type = Type.UNKNOWN;
    }
    System.out.println(type);
    return type;
  }

  // // Helper method that makes sure the password matches
  // private void passwordMatch() {
  //   String pass = request.getParameter("psw");
  //   String repeatPass = request.getParameter("psw-repeat");

  //   if(pass.equals(repeatPass)) {
  //     return pass;
  //   } else {

  //   }
  // }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Creating a user from the registration page
    final String emailEntered = request.getParameter("email");
    final String pswEntered = request.getParameter("psw");
    final String typeEntered = processType(request).toString();

    // Creating an UserEntity
    Entity taskEntity = new Entity("User Data");
    taskEntity.setProperty("Email", emailEntered);
    taskEntity.setProperty("Password", pswEntered);
    taskEntity.setProperty("Type", typeEntered);

    // Storing the Entity in datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    // After user registers, send to the dashboard page
    response.sendRedirect("/dashboard/index.html");
  }
>>>>>>> 039aac8d47f56de0a51e0ece519fdfedfded850b
}
