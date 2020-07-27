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
import com.google.sps.enums.EntityType;
import com.google.sps.exception.InvalidDateRangeException;
import com.google.sps.object.Office;
import com.google.sps.util.CoordinateCalculator;
import com.google.sps.util.OfficeManager;
import com.google.sps.util.QueryHelper;
import com.google.sps.util.GmapsHelper;
import com.google.maps.model.LatLng;
import java.io.IOException;
import com.google.maps.errors.ApiException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles adding and retreiving listings & locations */
@WebServlet("/locations")
public class LocationsServlet extends HttpServlet {

  private final int numListings = 10;
  GmapsHelper gmaps = GmapsHelper.getInstance();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Reading query string parameters
    String office = request.getParameter("office");
    Instant startDate = Instant.ofEpochMilli(Long.parseLong(request.getParameter("startMillis")));
    Instant endDate = Instant.ofEpochMilli(Long.parseLong(request.getParameter("endMillis")));

    Office selectedOffice = OfficeManager.offices.get(office);
    double distanceInKilometers = CoordinateCalculator.milesToKilometers(2.3);

    List<Entity> entityList =
        QueryHelper.getDistanceBasedEntities(
            EntityType.LISTING, numListings, selectedOffice, distanceInKilometers);

    try {
      entityList = runAllFiltersOnListings(entityList, selectedOffice, distanceInKilometers, startDate, endDate);
    } catch (InvalidDateRangeException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    } catch (ApiException | InterruptedException | IOException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(entityList));
  }

  private List<Entity> runAllFiltersOnListings(List<Entity> originalEntities, Office selectedOffice, double distanceInKilometers, Instant startDate, Instant endDate) throws InvalidDateRangeException, ApiException, InterruptedException, IOException {

    List<Entity> filteredEntities =
        CoordinateCalculator.filterOutOfRangeLatitudeEntities(
            distanceInKilometers,
            selectedOffice.getLatitude(),
            selectedOffice.getLongitude(),
            originalEntities);

    filteredEntities =
    QueryHelper.filterOutOfDateRangeListings(filteredEntities, startDate, endDate);

    int distanceFromOfficeKilometers = 2;
    LatLng officeCoordinates = new LatLng(selectedOffice.getLatitude(), selectedOffice.getLongitude());
    filteredEntities = QueryHelper.filterOutEntitiesWithGmapsRouteDistance(officeCoordinates, filteredEntities, distanceInKilometers, gmaps);

    return filteredEntities;
  }

  // MARK: POST
  @Override // Creates a new listing
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    createListing(request);
  }

  private void createListing(HttpServletRequest request) throws IOException {

    // To be used for timestamp
    long timestamp = System.currentTimeMillis();
    String userID = "0919199";

    // Create JSON object with GSON
    String requestData = request.getReader().lines().collect(Collectors.joining());
    JsonObject listingJson = new Gson().fromJson(requestData, JsonObject.class);

    // Creating DataStore Entity
    Entity taskEntity = new Entity(EntityType.LISTING.getValue());
    taskEntity.setProperty("userID", userID);
    taskEntity.setProperty("timestamp", timestamp);
    addJsonPropertiesToListingEntity(listingJson, taskEntity);

    // Placing Entity in datastore for persistant storage
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);
  }

  private void addJsonPropertiesToListingEntity(JsonObject listingJson, Entity taskEntity) {
    for (String key : listingJson.keySet()) {

      if (key.equals("contactInfo")) {
        taskEntity.setProperty(key, createEmbeddedEntity(listingJson.getAsJsonObject(key), false));

      } else if (key.equals("images")) {
        JsonArray jsonArray = listingJson.getAsJsonArray(key);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> imageLinkList = gson.fromJson(jsonArray.toString(), listType);
        taskEntity.setProperty(key, imageLinkList);

      } else if (key.equals("longitude") || key.equals("latitude")) {
        JsonElement element = listingJson.get(key);
        taskEntity.setProperty(key, element.getAsJsonPrimitive().getAsDouble());

      } else if (key.equals("listingStartDate") || key.equals("listingEndDate")) {
        JsonElement element = listingJson.get(key);
        Date millisToDate = new Date(element.getAsJsonPrimitive().getAsLong());
        taskEntity.setProperty(key, millisToDate);
      } else if (key.equals("amenities")) {
        taskEntity.setProperty(key, createEmbeddedEntity(listingJson.getAsJsonObject(key), true));
      } else {
        JsonElement element = listingJson.get(key);
        taskEntity.setProperty(key, element.getAsJsonPrimitive().getAsString());
      }
    }
  }

  /**
   * Returns an embedded entity created with the json object passed in
   *
   * @param contactInfo: Json object containing contact information
   * @return the created embedded entity
   */
  private EmbeddedEntity createEmbeddedEntity(JsonObject info, boolean saveAsBool) {
    EmbeddedEntity embeddedInfo = new EmbeddedEntity();
    for (String embeddedKey : info.keySet()) {
      JsonElement element = info.get(embeddedKey);
      if (saveAsBool) {
        embeddedInfo.setProperty(embeddedKey, element.getAsJsonPrimitive().getAsBoolean());
      } else {
        embeddedInfo.setProperty(embeddedKey, element.getAsJsonPrimitive().getAsString());
      }
    }

    return embeddedInfo;
  }
}