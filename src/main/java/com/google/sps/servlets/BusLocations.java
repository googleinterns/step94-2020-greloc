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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.sps.data.UserServiceHelper;
import com.google.sps.data.UserServiceHelper.Callback;
import com.google.sps.data.UserType;
import com.google.sps.enums.EntityType;
import com.google.sps.object.Office;
import com.google.sps.util.CoordinateCalculator;
import com.google.sps.util.OfficeManager;
import com.google.sps.util.QueryHelper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles adding and retreiving listings & locations */
@WebServlet("/busLocations")
public class BusLocations extends HttpServlet implements Callback {

  private final int numStops = 10;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserServiceHelper.authUser(this, response, request);
  }

  @Override // Creates a new BusStop
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserServiceHelper.authUser(this, response, request);
  }

  @Override
  public void handleResponse(
      HttpServletResponse response, HttpServletRequest request, UserType type) {
    try {
      if (request.getMethod().equals("GET")) {
        getBusStop(request, response);
      } else if (request.getMethod().equals("POST")) {
        createBusStop(request);
      }
    } catch (IOException e) {

    }
  }

  private void getBusStop(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String office = request.getParameter("office");
    Office selectedOffice = OfficeManager.offices.get(office);
    double distanceInKilometers = CoordinateCalculator.milesToKilometers(2.3);

    List<Entity> entityList =
        QueryHelper.getDistanceBasedEntities(
            EntityType.BUS_STOP, numStops, selectedOffice, distanceInKilometers);

    List<Entity> filteredEntityList =
        CoordinateCalculator.filterOutOfRangeLatitudeEntities(
            distanceInKilometers,
            selectedOffice.getLatitude(),
            selectedOffice.getLongitude(),
            entityList);

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(filteredEntityList));
  }

  private void createBusStop(HttpServletRequest request) throws IOException {

    // To be used for timestamp
    long timestamp = System.currentTimeMillis();

    // Create JSON object with GSON
    String requestData = request.getReader().lines().collect(Collectors.joining());
    JsonObject stopJson = new Gson().fromJson(requestData, JsonObject.class);

    // Creating DataStore Entity
    Entity taskEntity = new Entity(EntityType.BUS_STOP.getValue());
    taskEntity.setProperty("timestamp", timestamp);

    for (String key : stopJson.keySet()) {

      if (key.equals("longitude") || key.equals("latitude")) {
        JsonElement element = stopJson.get(key);
        taskEntity.setProperty(key, element.getAsJsonPrimitive().getAsDouble());
      } else {
        JsonElement element = stopJson.get(key);
        taskEntity.setProperty(key, element.getAsJsonPrimitive().getAsString());
      }
    }

    // Placing Entity in datastore for persistant storage
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);
  }
}
