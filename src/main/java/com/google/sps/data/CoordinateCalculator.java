package com.google.sps.data;
import java.util.List;
import java.util.ArrayList;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;

// Formulas from: https://stackoverflow.com/a/7478827
public final class CoordinateCalculator {
  private static final double earthRadius = 6378; // Kilometers

  public static double milesToKilometers(double miles){
    return miles * 1.60934;
  }

  /**
   * Adds a specified number of kilometers to a specified latitude and returns the result
   * @param kilometers: Number of kilometers to add/subtract to baseLong
   * @param baseLat: The original latitude
   */
  public static double calculateLatitudeCoordinateBound(double kilometers, double baseLat){
    return baseLat + (kilometers / earthRadius) * (180 / Math.PI);
  }

  /**
   * Adds a specified number of kilometers to a specified longitude and returns the result
   * @param kilometers: Number of kilometers to add/subtract to baseLong
   * @param baseLat: The original latitude
   * @param baseLong: The original longitude
   */
  public static double calculateLongitudeCoordinateBound(double kilometers, double baseLat, double baseLong){
    return baseLong + (kilometers / earthRadius) * (180 / Math.PI) / Math.cos(baseLat * Math.PI / 180);
  }

// OFFSETS
  public static double calculateLatitudeBoundOffset(double kilometers, double baseLat){
    return (kilometers / earthRadius) * (180 / Math.PI);
  }

  public static double calculateLongitudeBoundOffset(double kilometers, double baseLat, double baseLong){
    return (kilometers / earthRadius) * (180 / Math.PI) / Math.cos(baseLat * Math.PI / 180);
  }
// 
  public static CompositeFilter createLatitudeBoundFilter(double kilometers, double latitude){
    double latUpper = calculateLatitudeCoordinateBound(kilometers, latitude);
    double latLower = calculateLatitudeCoordinateBound(kilometers * -1, latitude);

    FilterPredicate upperBoundFilter = new Query.FilterPredicate("latitude", FilterOperator.LESS_THAN_OR_EQUAL, latUpper);
    FilterPredicate lowerBoundFilter = new Query.FilterPredicate("latitude", FilterOperator.GREATER_THAN_OR_EQUAL, latLower);

    return CompositeFilterOperator.and(upperBoundFilter, lowerBoundFilter);
  }

  public static CompositeFilter createLongitudeBoundFilter(double kilometers, double latitude, double longitude) {        
    double longUpper = calculateLongitudeCoordinateBound(kilometers, latitude, longitude);
    double longLower = calculateLongitudeCoordinateBound(kilometers * -1, latitude, longitude);

    FilterPredicate upperBoundFilter = new Query.FilterPredicate("longitude", FilterOperator.LESS_THAN_OR_EQUAL, longUpper);
    FilterPredicate lowerBoundFilter = new Query.FilterPredicate("longitude", FilterOperator.GREATER_THAN_OR_EQUAL, longLower);

    return CompositeFilterOperator.and(lowerBoundFilter, upperBoundFilter);
  }

  public static List<Entity> filterOutOfRangeLatitudeEntities(double kilometers, double baseLatitude, double baseLongitude, List<Entity> entities){
    double latUpperBound = calculateLatitudeCoordinateBound(kilometers, baseLatitude);
    double latLowerBound = calculateLatitudeCoordinateBound(kilometers * -1, baseLatitude);
    
    List<Entity> filteredList = new ArrayList<Entity>();
    for (Entity entity : entities) {

      double entityLat = (double) entity.getProperty("latitude");
      if ((entityLat <= latUpperBound) && (entityLat >= latLowerBound)) {    
        filteredList.add(entity);
      }
    }

    return filteredList;
  }
}