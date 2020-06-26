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

  enum CoordinateType {
    LATITUDE("latitude"), LONGITUDE("longitude");
    
    private final String value;
    private CoordinateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
  }

  private static class Bounds {
    double upper;
    double lower;
    
    public Bounds(double upper, double lower){
      this.upper = upper;
      this.lower = lower;
    }

    public double getUpperBound(){
      return this.upper;
    }

    public double getLowerBound(){
      return this.lower;
    }
  }

  private static final double earthRadius = 6371; // Kilometers

  public static double milesToKilometers(double miles){
    return miles * 1.60934;
  }

  /**
   * Returns a CompositeFilter to query any entities whose Latitude lies within (latitude + kilometers) and (latitude - kilometers)
   * @param kilometers: Distance, in kilometers, that each bound should be from the center point of the bounding box
   * @param latitude: the latitude of the center point of the bounding box
   * @param longitude: the longitude of the center point of the bounding box
   */
  public static CompositeFilter createLatitudeBoundFilter(double kilometers, double latitude){
    Bounds latBounds = calculateLatitudeBounds(kilometers, latitude);
    return createCoordinateBoundFilter(CoordinateType.LATITUDE, latBounds.getUpperBound(), latBounds.getLowerBound());
  }

  /**
   * Returns a CompositeFilter to query any entities whose Longitude lies within (longitude + kilometers) and (longitude - kilometers)
   * @param kilometers: Distance, in kilometers, that each bound should be from the center point of the bounding box
   * @param latitude: the latitude of the center point of the bounding box
   * @param longitude: the longitude of the center point of the bounding box
   */
  public static CompositeFilter createLongitudeBoundFilter(double kilometers, double latitude, double longitude) {        
    Bounds longBounds = calculateLongitudeBounds(kilometers, latitude, longitude);
    return createCoordinateBoundFilter(CoordinateType.LONGITUDE, longBounds.getUpperBound(), longBounds.getLowerBound());
  }

  /**
   * Returns a CompositeFilter to query any entities whose CoordinateType lies within a given upper and lower bound
   * @param coordinateType: CoordinateType.LATITUDE or CoordinateType.LONGITUDE 
   * @param upperBound: the longitude or latitude of the upper bound
   * @param lowerBound: the longitude or latitude of the lower bound
   */
  public static CompositeFilter createCoordinateBoundFilter(CoordinateType coordinateType, double upperBound, double lowerBound){
    FilterPredicate upperBoundFilter = new Query.FilterPredicate(coordinateType.value, FilterOperator.LESS_THAN_OR_EQUAL, upperBound);
    FilterPredicate lowerBoundFilter = new Query.FilterPredicate(coordinateType.value, FilterOperator.GREATER_THAN_OR_EQUAL, lowerBound);
    return CompositeFilterOperator.and(upperBoundFilter, lowerBoundFilter);    
  }
  
  /**
   * Returns a list of all Entities which have longitude value that falls within 
   * @param kilometers: Distance away from baseLatitude and baseLongitude, in kilometers 
   * @param baseLatitude: The latitude coordinate of the base location
   * @param baseLong: The longitude coordinate of the base location
   * @param entities: List of entities that have a "longitude" property
   */
  public static List<Entity> filterOutOfRangeLatitudeEntities(double kilometers, double baseLatitude, double baseLongitude, List<Entity> entities){
    
    Bounds latBounds = calculateLatitudeBounds(kilometers, baseLatitude);
    double latUpperBound = latBounds.getUpperBound();
    double latLowerBound = latBounds.getLowerBound();
    
    List<Entity> filteredList = new ArrayList<Entity>();
    for (Entity entity : entities) {
      printDebug(entity, kilometers, baseLatitude, baseLongitude, latBounds);
      double entityLat = (double) entity.getProperty(CoordinateType.LATITUDE.getValue());
      if ((entityLat <= latUpperBound) && (entityLat >= latLowerBound)) {    
        filteredList.add(entity);
      }
    }
    return filteredList;
  }

  private static void printDebug(Entity listing, double kilometers, double baseLatitude, double baseLongitude, Bounds latBounds){
    
    Bounds longBounds = calculateLongitudeBounds(kilometers, baseLatitude, baseLongitude);
    double listingLat = (double) listing.getProperty(CoordinateType.LATITUDE.getValue());
    double listingLong = (double) listing.getProperty(CoordinateType.LONGITUDE.getValue());
    
    System.out.println("---------- DEBUG ----------");
    System.out.println("(lat, long)");
    System.out.println("Office coordinates: (" + baseLatitude + ", " + baseLongitude + ")");
    System.out.println("Listing coordinates: (" + listingLat + ", " + listingLong + ")");
    
    System.out.println();
    
    System.out.println("Upper Bound coordinates: (" + latBounds.getUpperBound() + ", " + longBounds.getUpperBound() + ")");
    System.out.println("Lower Bound coordinates: (" + latBounds.getLowerBound() + ", " + longBounds.getLowerBound() + ")");
    
    System.out.println();
    
    System.out.println("listingLatitude: " + listingLat + " <= " + latBounds.getUpperBound() + " Upper Lat Bound");
    System.out.println("listingLatitude: " + listingLat + " >= " + latBounds.getLowerBound() + " Lower Lat Bound");
    
    System.out.println();
    
    System.out.println("listingLongitude: " + listingLong + " <= " + longBounds.getUpperBound() + " Upper Long Bound");
    System.out.println("listingLongitude: " + listingLong + " >= " + longBounds.getLowerBound() + " Lower Long Bound");
  }

  /**
   * Converts a distance in kilometers to an equivalent degree of latitude
   * @param kilometers: Distance to convert, in kilometers
   */
  private static double calculateLatitudeBoundOffset(double kilometers){
    return (kilometers / earthRadius) * (180 / Math.PI);
  }

  /**
   * Converts a distance in kilometers with a given latitude to an equivalent degree of longitude
   * @param kilometers: Distance to convert, in kilometers
   * @param baseLat: The latitude coordinate pair of the longitude to which this offset will be added/subtracted
   */
  private static double calculateLongitudeBoundOffset(double kilometers, double baseLat){
    return (kilometers / earthRadius) * (180 / Math.PI) / Math.cos(baseLat * Math.PI/180);
  }

  private static Bounds calculateLatitudeBounds(double kilometers, double latitude){
    double latitudeOffsetHi = calculateLatitudeBoundOffset(kilometers);
    double latitudeOffsetLo = calculateLatitudeBoundOffset(kilometers * -1);
    double latUpper = latitude + latitudeOffsetHi;
    double latLower = latitude + latitudeOffsetLo;
    return new Bounds(latUpper, latLower);
  }

  private static Bounds calculateLongitudeBounds(double kilometers, double latitude, double longitude) {
    double longitudeOffsetHi = calculateLongitudeBoundOffset(kilometers, latitude);
    double longitudeOffsetLo = calculateLongitudeBoundOffset((kilometers * -1), latitude);
    double longUpper = longitude + longitudeOffsetHi;
    double longLower = longitude + longitudeOffsetLo;
    return new Bounds(longUpper, longLower);
  }

  // https://stackoverflow.com/a/16794680
  public static double distance(double lat1, double lat2, double lon1,
  double lon2, double el1, double el2) {

    final int R = 6371; // Radius of the earth

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
          + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
          * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = el1 - el2;

    distance = Math.pow(distance, 2) + Math.pow(height, 2);

    return Math.sqrt(distance);
  }

}