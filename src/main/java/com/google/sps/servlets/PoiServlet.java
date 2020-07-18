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
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.sps.data.CoordinateCalculator;
import com.google.sps.data.EntityType;
import com.google.sps.data.InvalidDateRangeException;
import com.google.sps.data.Office;
import com.google.sps.data.OfficeManager;
import com.google.sps.data.QueryHelper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.util.GmapsHelper;
import com.google.sps.data.util.CategoryGroup;
import com.google.maps.errors.ApiException;
import java.io.IOException;


/** Servlet that handles adding and retreiving listings & locations */
@WebServlet("/poi")
public class PoiServlet extends HttpServlet {

  private final GmapsHelper gmapsHelper = new GmapsHelper(true);
  private final int radiusMeters = 5000;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String office = request.getParameter("office");
    int poiGroup = Integer.parseInt(request.getParameter("group"));
    Office selectedOffice = OfficeManager.offices.get(office);

    CategoryGroup categoryGroup = gmapsHelper.getCategoryGroupById(poiGroup);
    try {
      String results = gmapsHelper.searchNearbyCategoryGroup(categoryGroup, selectedOffice.getLatitude(), selectedOffice.getLongitude(), radiusMeters);
      response.setContentType("application/json;");
      response.getWriter().println(results);      
    } catch (ApiException e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

  }

}