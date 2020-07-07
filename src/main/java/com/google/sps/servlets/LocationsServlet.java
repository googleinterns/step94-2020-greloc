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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.sps.data.CoordinateCalculator;
import com.google.sps.data.EntityType;
import com.google.sps.data.Office;
import com.google.sps.data.OfficeManager;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles adding and retreiving listings & locations */
@WebServlet("/locations")
public class LocationsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String office = request.getParameter("office");
    Office selectedOffice = OfficeManager.offices.get(office);
    double distanceInKilometers = CoordinateCalculator.milesToKilometers(2.3);

    List<Entity> entityList =
        getDistanceBasedListings(request, selectedOffice, distanceInKilometers);
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

  private List<Entity> getDistanceBasedListings(
      HttpServletRequest request, Office selectedOffice, double kilometers) {
    CompositeFilter distanceFromOfficeFilter =
        CoordinateCalculator.createLongitudeBoundFilter(
            kilometers, selectedOffice.getLatitude(), selectedOffice.getLongitude());

    // Retrieving Listings from DataStore
    Query query = new Query("Listing").setFilter(distanceFromOfficeFilter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    return results.asList(FetchOptions.Builder.withLimit(10));
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
    Entity taskEntity = new Entity(EntityType.BUS_STOP.getValue());
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
        taskEntity.setProperty(key, createEmbeddedContactInfo(listingJson.getAsJsonObject(key)));
      } else if (key.equals("images")) {
        JsonArray jsonArray = listingJson.getAsJsonArray(key);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> imageLinkList = gson.fromJson(jsonArray.toString(), listType);

        taskEntity.setProperty(key, imageLinkList);
      } else if (key.equals("longitude") || key.equals("latitude")) {
        JsonElement element = listingJson.get(key);
        taskEntity.setProperty(key, element.getAsJsonPrimitive().getAsDouble());
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
  private EmbeddedEntity createEmbeddedContactInfo(JsonObject contactInfo) {
    EmbeddedEntity embeddedContactInfo = new EmbeddedEntity();
    for (String embeddedKey : contactInfo.keySet()) {
      JsonElement element = contactInfo.get(embeddedKey);
      embeddedContactInfo.setProperty(embeddedKey, element.getAsJsonPrimitive().getAsString());
    }

    return embeddedContactInfo;
  }
}
