package com.google.sps.util;

import com.google.gson.Gson;
import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.google.sps.enums.CategoryGroup;
import com.google.sps.exception.InvalidCategoryGroupException;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.util.Arrays;

public class GmapsHelper {

  GeoApiContext context;

  private final PlaceType[] recreationTypes = {
    PlaceType.AMUSEMENT_PARK,
    PlaceType.AQUARIUM,
    PlaceType.ART_GALLERY,
    PlaceType.BOWLING_ALLEY,
    PlaceType.CAMPGROUND,
    PlaceType.GYM,
    PlaceType.MOVIE_THEATER,
    PlaceType.MUSEUM,
    PlaceType.NIGHT_CLUB,
    PlaceType.PARK,
    PlaceType.SPA,
    PlaceType.STADIUM,
    PlaceType.SHOPPING_MALL,
    PlaceType.TOURIST_ATTRACTION
  };

  private final PlaceType[] diningTypes = {
    PlaceType.BAKERY,
    PlaceType.BAR,
    PlaceType.CAFE,
    PlaceType.BOWLING_ALLEY,
    PlaceType.MEAL_DELIVERY,
    PlaceType.MEAL_TAKEAWAY,
    PlaceType.RESTAURANT
  };

  private final PlaceType[] groceryTypes = {
    PlaceType.GROCERY_OR_SUPERMARKET, PlaceType.PHARMACY, PlaceType.PET_STORE, PlaceType.DRUGSTORE
  };

  public GmapsHelper(boolean useAppEngineContextBuilder) {
    Dotenv dotenv = Dotenv.load();
    String key = dotenv.get("GMAPS_API_KEY");

    if (useAppEngineContextBuilder) {
      this.context = new GeoApiContext.Builder(new GaeRequestHandler.Builder()).apiKey(key).build();
    } else {
      this.context = new GeoApiContext.Builder().apiKey(key).build();
    }
  }

  /**
   * Converts an int Id to a CategoryGroup enum
   *
   * @param Id: An integer between 2-4 (inclusive)
   * @return The corresponding CategoryGroup to Id
   */
  public CategoryGroup getCategoryGroupById(int Id) throws InvalidCategoryGroupException {
    switch (Id) {
      case 2:
        return CategoryGroup.RECREATION;
      case 3:
        return CategoryGroup.DINING;
      case 4:
        return CategoryGroup.GROCERY;
      default:
        throw new InvalidCategoryGroupException("ID " + Id + " is not a valid ID");
    }
  }

  /**
   * Searches for places within `radiusMeters` nearby `anchorLatitude` and `anchorLongitude`
   *
   * @param categoryGroup: categoryGroup for which to search
   * @param anchorLatitude: The latitude coordinate of the location to search around
   * @param anchorLongitude: The longitude coordinate of the location to search around
   * @param radiusMeters: The radius, in meters, to search around the specified `anchorLatitude` and
   *     `anchorLongitude`
   * @return A valid JSON string containing the results
   */
  public String searchNearbyCategoryGroup(
      CategoryGroup categoryGroup, double anchorLatitude, double anchorLongitude, int radiusMeters)
      throws ApiException, InterruptedException, IOException {
    PlaceType[] placeTypes;
    switch (categoryGroup) {
      case RECREATION:
        placeTypes = this.recreationTypes;
        break;
      case DINING:
        placeTypes = this.diningTypes;
        break;
      case GROCERY:
        placeTypes = this.groceryTypes;
        break;
      default:
        placeTypes = new PlaceType[0];
    }

    PlacesSearchResult[] results =
        this.nearbySearchMultipleType(placeTypes, anchorLatitude, anchorLongitude, radiusMeters);
    Gson gson = new Gson();
    return gson.toJson(results);
  }

  private PlacesSearchResult[] nearbySearchMultipleType(
      PlaceType[] types, double anchorLatitude, double anchorLongitude, int radiusMeters)
      throws ApiException, InterruptedException, IOException {
    PlacesSearchResult[] accumulatedResults = new PlacesSearchResult[0];
    for (PlaceType type : types) {
      PlacesSearchResult[] iterationResults =
          this.nearbySearchByPlaceType(type, anchorLatitude, anchorLongitude, radiusMeters);
      accumulatedResults =
          Arrays.copyOf(accumulatedResults, accumulatedResults.length + iterationResults.length);
      System.arraycopy(
          iterationResults,
          0,
          accumulatedResults,
          (accumulatedResults.length - iterationResults.length),
          iterationResults.length);
    }
    return accumulatedResults;
  }

  public PlacesSearchResult[] nearbySearchByPlaceType(
      PlaceType placeType, double anchorLatitude, double anchorLongitude, int radiusMeters)
      throws ApiException, InterruptedException, IOException {
    LatLng location = new LatLng(anchorLatitude, anchorLongitude);
    PlacesSearchResponse response =
        PlacesApi.nearbySearchQuery(this.context, location)
            .radius(radiusMeters)
            .rankby(RankBy.PROMINENCE)
            .type(placeType)
            .language("en")
            .await();
    return response.results;
  }
}
