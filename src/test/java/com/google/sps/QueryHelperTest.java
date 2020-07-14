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

package com.google.sps.data;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class QueryHelperTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private DatastoreService ds;
  private final Office TEST_OFFICE = new Office("test", 1, 1);

  private final String JULY_LISTING_ID = "JULY_11_21";
  private final String AUGUST_LISTING_ID = "AUGUST_2_15";

  private final long JULY_ELEVENTH = 1594504312;
  private final long JULY_TWENTY_FIRST = 1595368312;

  private final long AUGUST_SECOND = 1596354860;
  private final long AUGUST_FIFTEENTH = 1597478060;

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

    long oneDayAfterEleventh =
        Instant.ofEpochMilli(JULY_ELEVENTH * 1000).plus(1, ChronoUnit.DAYS).getEpochSecond();

    long oneDayBeforeTwentieth =
        Instant.ofEpochMilli(JULY_TWENTY_FIRST * 1000).minus(1, ChronoUnit.DAYS).getEpochSecond();

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

    long oneDayBeforeEleventh =
        Instant.ofEpochMilli(JULY_ELEVENTH * 1000).minus(1, ChronoUnit.DAYS).getEpochSecond();

    List<Entity> results =
        QueryHelper.filterOutOfDateRangeListings(listings, oneDayBeforeEleventh, JULY_TWENTY_FIRST);
    Assert.assertEquals(0, results.size());
  }

  @Test
  public void dateRangeDoesNotReturnOutOfUpperBound() throws Exception {

    List<Entity> listings = getDateRangeTestingEntityList();

    long oneDayAfterTwentyFirst =
        Instant.ofEpochMilli(JULY_TWENTY_FIRST * 1000).plus(1, ChronoUnit.DAYS).getEpochSecond();

    List<Entity> results =
        QueryHelper.filterOutOfDateRangeListings(listings, JULY_ELEVENTH, oneDayAfterTwentyFirst);
    Assert.assertEquals(0, results.size());
  }

  @Test(expected = InvalidDateRangeException.class)
  public void dateRangeThrowsExceptionWithBadRangeParameters() throws Exception {
    List<Entity> listings = getDateRangeTestingEntityList();

    long julyTwentySecond =
        Instant.ofEpochMilli(JULY_TWENTY_FIRST * 1000).plus(1, ChronoUnit.DAYS).getEpochSecond();

    QueryHelper.filterOutOfDateRangeListings(listings, julyTwentySecond, JULY_TWENTY_FIRST);
  }

  private void insertNearListing() {

    // Located roughly 1km from (1,1)
    Entity testListingNear = new Entity("Listing");
    testListingNear.setProperty("name", "near");
    testListingNear.setProperty("latitude", 1.0063);
    testListingNear.setProperty("longitude", 1.0063);

    ds.put(testListingNear);

    Assert.assertEquals(1, ds.prepare(new Query("Listing")).countEntities(withLimit(10)));
  }

  private void insertFarListing() {

    // Located roughly 10km from (1,1)
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
    inDateRangeEntity.setProperty("listingStartTimestamp", JULY_ELEVENTH);
    inDateRangeEntity.setProperty("listingEndTimestamp", JULY_TWENTY_FIRST);

    // Entity listed from July August 2nd to August 15th
    Entity outDateRangeEntity = new Entity("Listing");
    outDateRangeEntity.setProperty("name", AUGUST_LISTING_ID);
    outDateRangeEntity.setProperty("listingStartTimestamp", AUGUST_SECOND);
    outDateRangeEntity.setProperty("listingEndTimestamp", AUGUST_FIFTEENTH);

    testingListings.add(inDateRangeEntity);
    testingListings.add(outDateRangeEntity);

    return testingListings;
  }
}
