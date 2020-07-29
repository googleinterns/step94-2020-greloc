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

package com.google.sps.util;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.maps.model.LatLng;
import com.google.sps.enums.EntityType;
import com.google.sps.exception.InvalidDateRangeException;
import com.google.sps.object.Office;
import com.google.maps.model.LatLng;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public final class QueryHelperTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private DatastoreService ds;
  private final Office TEST_OFFICE = new Office("test", 1, 1);
  private final Office ROUTE_TEST_OFFICE = new Office("Google_TC3", 37.403001, -122.032618);
  
  private final int ROUTE_DISTANCE_FROM_OFFICE_KM = 2;

  private final String JULY_LISTING_ID = "JULY_11_21";
  private final String AUGUST_LISTING_ID = "AUGUST_2_15";

  private final Instant JULY_ELEVENTH = Instant.ofEpochMilli(1594504312000L);
  private final Instant JULY_TWENTY_FIRST = Instant.ofEpochMilli(1595368312000L);

  private final Instant AUGUST_SECOND = Instant.ofEpochMilli(1596354860000L);
  private final Instant AUGUST_FIFTEENTH = Instant.ofEpochMilli(1597478060000L);

  @Before
  public void setUp() {
    helper.setUp();
    ds = DatastoreServiceFactory.getDatastoreService();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testCleanEnvironment() {
    Assert.assertEquals(0, ds.prepare(new Query("Listing")).countEntities(withLimit(10)));
  }

  // MARK: getDistanceBasedEntities
  @Test
  public void testDistanceBasedEntitiesReturnsInRange() {
    insertNearListing();
    int distanceFromOfficeKilometers = 2;

    List<Entity> results =
        QueryHelper.getDistanceBasedEntities(
            EntityType.LISTING, 10, TEST_OFFICE, distanceFromOfficeKilometers);
    Assert.assertEquals(1, results.size());

    String listingName = (String) results.get(0).getProperty("name");
    Assert.assertEquals("near", listingName);
  }

  @Test
  public void testDistanceBasedEntitiesDoesNotReturnOutOfRange() {
    insertFarListing();
    int distanceFromOfficeKilometers = 2;

    List<Entity> results =
        QueryHelper.getDistanceBasedEntities(
            EntityType.LISTING, 10, TEST_OFFICE, distanceFromOfficeKilometers);
    Assert.assertEquals(0, results.size());
  }

  // MARK: filterOutOfDateRangeListings
  @Test
  public void testDateRangeReturnsInBounds() throws Exception {

    List<Entity> listings = getDateRangeTestingEntityList();

    Instant oneDayAfterEleventh = JULY_ELEVENTH.plus(1, ChronoUnit.DAYS);

    Instant oneDayBeforeTwentieth = JULY_TWENTY_FIRST.minus(1, ChronoUnit.DAYS);

    List<Entity> results =
        QueryHelper.filterOutOfDateRangeListings(
            listings, oneDayAfterEleventh, oneDayBeforeTwentieth);
    Assert.assertEquals(1, results.size());

    String listingId = (String) results.get(0).getProperty("name");
    Assert.assertEquals(listingId, JULY_LISTING_ID);
  }

  @Test
  public void testDateRangeReturnsInBoundsOnExactDates() throws Exception {

    List<Entity> listings = getDateRangeTestingEntityList();

    List<Entity> results =
        QueryHelper.filterOutOfDateRangeListings(listings, JULY_ELEVENTH, JULY_TWENTY_FIRST);
    Assert.assertEquals(1, results.size());

    String listingId = (String) results.get(0).getProperty("name");
    Assert.assertEquals(listingId, JULY_LISTING_ID);
  }

  @Test
  public void testDateRangeDoesNotReturnOutOfLowerBound() throws Exception {

    List<Entity> listings = getDateRangeTestingEntityList();

    Instant oneDayBeforeEleventh = JULY_ELEVENTH.minus(1, ChronoUnit.DAYS);

    List<Entity> results =
        QueryHelper.filterOutOfDateRangeListings(listings, oneDayBeforeEleventh, JULY_TWENTY_FIRST);
    Assert.assertEquals(0, results.size());
  }

  @Test
  public void dateRangeDoesNotReturnOutOfUpperBound() throws Exception {

    List<Entity> listings = getDateRangeTestingEntityList();

    Instant oneDayAfterTwentyFirst = JULY_TWENTY_FIRST.plus(1, ChronoUnit.DAYS);

    List<Entity> results =
        QueryHelper.filterOutOfDateRangeListings(listings, JULY_ELEVENTH, oneDayAfterTwentyFirst);
    Assert.assertEquals(0, results.size());
  }

  @Test(expected = InvalidDateRangeException.class)
  public void dateRangeThrowsExceptionWithBadRangeParameters() throws Exception {
    List<Entity> listings = getDateRangeTestingEntityList();

    Instant julyTwentySecond = JULY_TWENTY_FIRST.plus(1, ChronoUnit.DAYS);

    QueryHelper.filterOutOfDateRangeListings(listings, julyTwentySecond, JULY_TWENTY_FIRST);
  }

  @Test
  public void testFilterOutEntitiesWithGmapsRouteDistanceReturnsInRange() throws Exception {

    GmapsHelper mockGmaps = setUpGmapsMock();

    List<Entity> listings = getGmapsDistanceTestEntities();    
    LatLng testOfficeCoordinates = new LatLng(ROUTE_TEST_OFFICE.getLatitude(), ROUTE_TEST_OFFICE.getLongitude());
    List<Entity> results = QueryHelper.filterOutEntitiesWithGmapsRouteDistance(testOfficeCoordinates, listings, ROUTE_DISTANCE_FROM_OFFICE_KM, mockGmaps);
        
    Assert.assertEquals(1, results.size());
    Assert.assertEquals("near", (String) results.get(0).getProperty("name"));
  }

  @Test
  public void testFilterOutEntitiesWithGmapsRouteDistanceDoesNotReturnOutOfRange() throws Exception {
    
    GmapsHelper mockGmaps = setUpGmapsMock();

    int distanceTooCloseToGetAnyResultsKilometers = ROUTE_DISTANCE_FROM_OFFICE_KM - 1;
    List<Entity> listings = getGmapsDistanceTestEntities();
    LatLng testOfficeCoordinates = new LatLng(ROUTE_TEST_OFFICE.getLatitude(), ROUTE_TEST_OFFICE.getLongitude());
    List<Entity> results = QueryHelper.filterOutEntitiesWithGmapsRouteDistance(testOfficeCoordinates, listings, distanceTooCloseToGetAnyResultsKilometers, mockGmaps);
        
    Assert.assertEquals(0, results.size());
  }

  // MARK: Helpers
  private void insertNearListing() {

    // Located roughly 1km linear distance from (1,1)
    Entity testListingNear = new Entity("Listing");
    testListingNear.setProperty("name", "near");
    testListingNear.setProperty("latitude", 1.0063);
    testListingNear.setProperty("longitude", 1.0063);

    ds.put(testListingNear);

    Assert.assertEquals(1, ds.prepare(new Query("Listing")).countEntities(withLimit(10)));
  }

  private void insertFarListing() {

    // Located roughly 10km linear distance from (1,1)
    Entity testListingFar = new Entity("Listing");
    testListingFar.setProperty("name", "far");
    testListingFar.setProperty("latitude", 1.063);
    testListingFar.setProperty("longitude", 1.063);

    ds.put(testListingFar);

    Assert.assertEquals(1, ds.prepare(new Query("Listing")).countEntities(withLimit(10)));
  }

  private List<Entity> getDateRangeTestingEntityList() {

    List<Entity> testingListings = new ArrayList<>();

    // Entity listed from July 11th to July 21st
    Entity inDateRangeEntity = new Entity("Listing");
    inDateRangeEntity.setProperty("name", JULY_LISTING_ID);
    inDateRangeEntity.setProperty("listingStartDate", Date.from(JULY_ELEVENTH));
    inDateRangeEntity.setProperty("listingEndDate", Date.from(JULY_TWENTY_FIRST));

    // Entity listed from July August 2nd to August 15th
    Entity outDateRangeEntity = new Entity("Listing");
    outDateRangeEntity.setProperty("name", AUGUST_LISTING_ID);
    outDateRangeEntity.setProperty("listingStartDate", Date.from(AUGUST_SECOND));
    outDateRangeEntity.setProperty("listingEndDate", Date.from(AUGUST_FIFTEENTH));

    testingListings.add(inDateRangeEntity);
    testingListings.add(outDateRangeEntity);

    return testingListings;
  }

  private List<Entity> getGmapsDistanceTestEntities() {

    List<Entity> testingListings = new ArrayList<>();

    // Located rougly 1.77028km DRIVING distance from ROUTE_TEST_OFFICE
    Entity testListingNear = new Entity("Listing");
    testListingNear.setProperty("name", "near");
    testListingNear.setProperty("latitude", 37.396158);
    testListingNear.setProperty("longitude", -122.027779);

    // Located rougly 4.18429km DRIVING distance from ROUTE_TEST_OFFICE
    Entity testListingFar = new Entity("Listing");
    testListingFar.setProperty("name", "far");
    testListingFar.setProperty("latitude", 37.373779);
    testListingFar.setProperty("longitude", -122.032289);

    testingListings.add(testListingNear);
    testingListings.add(testListingFar);

    return testingListings;
  }

  private GmapsHelper setUpGmapsMock() throws Exception {
    final GmapsHelper mockGmaps = Mockito.mock(GmapsHelper.class);
    DistanceMatrixRow[] mockRows = getMockMatrixRows();
    
    Mockito.doReturn(mockRows)
      .when(mockGmaps)
      .routeDistanceBetweenPoints(
          ArgumentMatchers.any(LatLng.class),
          ArgumentMatchers.<LatLng>any());
    return mockGmaps;
  }

  private DistanceMatrixRow[] getMockMatrixRows() {
    
    final DistanceMatrixRow mockNearRow = Mockito.mock(DistanceMatrixRow.class);
    final DistanceMatrixRow mockFarRow = Mockito.mock(DistanceMatrixRow.class);

    final DistanceMatrixElement mockNearElement = Mockito.mock(DistanceMatrixElement.class);
    final DistanceMatrixElement mockFarElement = Mockito.mock(DistanceMatrixElement.class);

    final Distance mockNearDistance = Mockito.mock(Distance.class);
    final Distance mockFarDistance = Mockito.mock(Distance.class);

    mockNearDistance.inMeters = 1500;
    mockFarDistance.inMeters = 5000;

    mockNearElement.distance = mockNearDistance;
    mockFarElement.distance = mockFarDistance;

    mockNearRow.elements = new DistanceMatrixElement[]{mockNearElement};
    mockFarRow.elements = new DistanceMatrixElement[]{mockFarElement};

    return new DistanceMatrixRow[]{mockNearRow, mockFarRow};
  }
}
