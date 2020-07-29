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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.UserHelper;
import com.google.sps.data.UserType;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* This class handles authorization of the user*/

@WebServlet("/authorization")
public class AuthorizationServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");

    UserService userService = UserServiceFactory.getUserService();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserHelper userHelper = new UserHelper(datastore);

    // Checking to see if user has an account in the entity
    if (userHelper.doesUserEmailExist(request)) {
      response.sendRedirect("/dashboard/index.html");
      return;
    } else {
      response.sendRedirect("/registration.html");
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    // Creating a user from the registration page
    final String emailEntered = request.getUserPrincipal().getName();
    String userID = userService.getCurrentUser().getUserId();
    UserType typeEnteredEnum = processType(request);
    final int typeEnteredInt = typeEnteredEnum.getValue();

    // Creating an UserEntity
    Entity taskEntity = new Entity("UserData");
    taskEntity.setProperty("userID", userID);
    taskEntity.setProperty("Email", emailEntered);
    taskEntity.setProperty("Type", typeEnteredInt);

    // Storing the Entity in datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    // After user email is stored in database, send to the dashboard page
    response.sendRedirect("/dashboard/index.html");
  }

  // A helper method that helps identifies the UserType
  public static UserType processType(HttpServletRequest request) {
    UserType type = null;
    String userType = request.getParameter("type");
    if ("renter".equals(userType)) {
      type = UserType.RENTER;
    } else if ("host".equals(userType)) {
      type = UserType.HOST;
    } else if ("both".equals(userType)) {
      type = UserType.BOTH;
    } else if (userType == null) {
      type = UserType.UNKNOWN;
    }
    return type;
  }
}
