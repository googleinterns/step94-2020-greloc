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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/authentication")
public class AuthenticationServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    PrintWriter out = response.getWriter();

    UserService userService = UserServiceFactory.getUserService();
    
    // Actions after users are authorized
    if (userService.isUserLoggedIn() /*&& userService.getCurrentUser().getAuthDomain() == "google.com" */) {
      response.sendRedirect("/dashboard/index.html");
      String urlToRedirectToAfterUserLogsOut = "/index.html";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

      // Actions when users are not authorized
    } else {
      System.out.println("NOT LOGGED IN");
      String loginUrl = userService.createLoginURL("/authentication");
      out.println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");

    }
  }
}