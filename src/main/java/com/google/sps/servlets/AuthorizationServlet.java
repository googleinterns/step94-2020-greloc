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
import java.util.Arrays;
 
/* This class handles authorization of the user*/
 
@WebServlet("/authorization")
public class AuthorizationServlet extends HttpServlet {
 
 public enum Type {
   RENTER,HOST,BOTH,UNKNOWN;
 }
  @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
   response.setContentType("text/html;");
   PrintWriter out = response.getWriter();
 
   UserService userService = UserServiceFactory.getUserService();
  
   //Creating a Query to search through the datastore
   Query query = new Query("User Data");
   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();  
   PreparedQuery results = datastore.prepare(query);
 
   final String emailLogin = request.getUserPrincipal().toString();
  
   //Iterating through the entity
   for(Entity entity: results.asIterable()) {
     String userEmail = (String) entity.getProperty("Email");
     String userType = (String) entity.getProperty("Type");
 
     //Checking to see if user has an account in the entity
     if(emailLogin.equals(userEmail) && userType != null) {
       System.out.println("Email thats trying to login: " + emailLogin);
       System.out.println("Email thats trying to match from entity: " + userEmail);
       System.out.println("Its a match!");
       System.out.println("BREAK");
        if(userType == null) {
          System.out.println("This user does not have a userType");
          response.sendRedirect("/registration.html");
          return;
        }
       response.sendRedirect("/dashboard/index.html"); 
       return;
     } if(emailLogin == null) {
       System.out.println("EMAIL IS NULL!!");
       response.sendRedirect("/registration.html");
       return;
     }
   }
   System.out.println("The emails don't match");
   response.sendRedirect("/registration.html");
   }
 
 //A helper method that helps identifies the UserType
 private Type processType(HttpServletRequest request) {
   Type type = null;
   String userType = request.getParameter("type");
   if ("renter".equals(userType)) {
       type = Type.RENTER;
     } else if ("host".equals(userType)) {
       type = Type.HOST;
     } else if ("both".equals(userType)) {
       type = Type.BOTH;
     } else if(userType == null) {
       type = Type.UNKNOWN;
     }
   return type;
 }
 
 public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
   

   //Creating a user from the registration page
   final String emailEntered = request.getUserPrincipal().toString();
   final String typeEntered = processType(request).toString();
 
   //Creating an UserEntity
   Entity taskEntity = new Entity("User Data");
   taskEntity.setProperty("Email", emailEntered);
   taskEntity.setProperty("Type", typeEntered);
 
   //Storing the Entity in datastore
   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
   datastore.put(taskEntity);
 
   //After user is logged in, send to the dashboard
   response.sendRedirect("/dashboard/index.html");
 }
 
}
