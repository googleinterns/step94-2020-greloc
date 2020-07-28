package com.google.sps.util;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.google.sps.enums.EntityType;
import com.google.sps.exception.InvalidDateRangeException;
import com.google.sps.object.Office;
import com.google.sps.util.CoordinateCalculator.CoordinateType;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class QueryHelper {

  /**
   * Returns a list of entities that have been filtered based on their longitude in relation to the
   * selected office's longitude
   *
   * @param entityType: The type of entity to be queried
   * @param entityLimit: The max number of entityType to return
   * @param selectedOffice: The office for which to find nearby entities
   * @param kilometers: Distance in kilometers to create a range with from the office's center
   * @return List of filtered entities
   */
  public static List<Entity> getDistanceBasedEntities(
      EntityType entityType, int entityLimit, Office selectedOffice, double kilometers) {

    CompositeFilter distanceFromOfficeFilter =
        CoordinateCalculator.createLongitudeBoundFilter(
            kilometers, selectedOffice.getLatitude(), selectedOffice.getLongitude());

    // Retrieving Listings from DataStore
    Query query = new Query(entityType.getValue()).setFilter(distanceFromOfficeFilter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    return results.asList(FetchOptions.Builder.withLimit(entityLimit));
  }

  /**
   * Lorem ipsum
   *
   * @param entities: The type of entity to be queried
   * @param origin: The max number of entityType to return
   * @param kilometers: Distance in kilometers to create a range with from the office's center
   * @return List of filtered entities
   */
  public static List<Entity> filterOutEntitiesWithGmapsRouteDistance(
      LatLng destination,
      List<Entity> originEntities,
      double distanceLimitKilometers,
      GmapsHelper gmaps)
      throws ApiException, InterruptedException, IOException {

    LatLng[] origins = new LatLng[originEntities.size()];
    for (int i = 0; i < originEntities.size(); i++) {
      Entity entity = originEntities.get(i);
      double entityLat = (double) entity.getProperty(CoordinateType.LATITUDE.getValue());
      double entityLong = (double) entity.getProperty(CoordinateType.LONGITUDE.getValue());
      origins[i] = new LatLng(entityLat, entityLong);
    }

    DistanceMatrixRow[] results = gmaps.routeDistanceBetweenPoints(destination, origins);

    List<Entity> filteredList = new ArrayList<>();
    for (int i = 0; i < results.length; i++) {
      double distanceLimitMeters = (distanceLimitKilometers * 1000);
      if (results[i].elements[0].distance.inMeters < distanceLimitMeters) {
        filteredList.add(originEntities.get(i));
      }
    }

    return filteredList;
  }

  /**
   * Filters out entities that aren't listed within a give start and end timestamp
   *
   * @param listings: List of entities with listingStartTimestamp and listingEndTimestamp properties
   * @param baseStart: Start of date range epoch
   * @param baseEnd: End of date range epoch
   * @return List of filtered entities
   */
  public static List<Entity> filterOutOfDateRangeListings(
      List<Entity> listings, Instant dateRangeStart, Instant dateRangeEnd)
      throws InvalidDateRangeException {

    if (dateRangeStart.isAfter(dateRangeEnd)) {
      throw new InvalidDateRangeException("Invalid Range: dateRangeStart is after dateRangeEnd");
    }

    List<Entity> filteredListings = new ArrayList<>();
    for (Entity listing : listings) {
      Instant listingStartInstant = ((Date) listing.getProperty("listingStartDate")).toInstant();
      Instant listingEndInstant = ((Date) listing.getProperty("listingEndDate")).toInstant();

      if (listingStartInstant.compareTo(dateRangeStart) <= 0
          && listingEndInstant.compareTo(dateRangeEnd) >= 0) {
        filteredListings.add(listing);
      }
    }

    return filteredListings;
  }

  public static List<Entity> getUserListings(EntityType entityType, String userID) {

    Filter propertyFilter = new FilterPredicate("userID", FilterOperator.EQUAL, userID);
    Query query = new Query("Listing").setFilter(propertyFilter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    return results.asList(FetchOptions.Builder.withLimit(10));
  }
}
