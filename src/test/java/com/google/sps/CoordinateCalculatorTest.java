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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.After;
import org.junit.Before;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import java.util.List;
import java.util.ArrayList;

import com.google.appengine.api.datastore.Entity;

@RunWith(JUnit4.class)
public final class CoordinateCalculatorTest {
  private final double marginError = 0.0001;
  private final int distanceFromOfficeInKilometers = 1;
  private final int baseLatitude = 1;
  private final int baseLongitude = 1;

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testEnumValueLatitude() {
    String latitudeEnumValue = CoordinateCalculator.CoordinateType.LATITUDE.getValue();
    Assert.assertEquals("latitude", latitudeEnumValue);
  }

  @Test
  public void testEnumValueLongitude() {
    String latitudeEnumValue = CoordinateCalculator.CoordinateType.LONGITUDE.getValue();
    Assert.assertEquals("longitude", latitudeEnumValue);
  }

  @Test
  public void testMilesToKilometersConversion() {

    double miles = 10;
    double kilometers = CoordinateCalculator.milesToKilometers(miles);
    
    double expectedKilometers = 16.0934;
    Assert.assertEquals(expectedKilometers, kilometers, marginError);
  }

  @Test
  public void testFilterOutOfRangeLatitudeEntitiesNotEmpty() {
    
    List<Entity> unfilteredList = createTestListingsList();
    List<Entity> filteredList = CoordinateCalculator.filterOutOfRangeLatitudeEntities(distanceFromOfficeInKilometers, baseLatitude, baseLongitude, unfilteredList);

    Assert.assertEquals(1, filteredList.size());
  }

  @Test
  public void testFilterOutOfRangeLatitudeEntitiesReturnsInRange() {
    
    List<Entity> unfilteredList = createTestListingsList();
    List<Entity> filteredList = CoordinateCalculator.filterOutOfRangeLatitudeEntities(distanceFromOfficeInKilometers, baseLatitude, baseLongitude, unfilteredList);

    String listingName = (String) filteredList.get(0).getProperty("name");
    Assert.assertEquals("near", listingName);
  }

  private List<Entity> createTestListingsList() {
    List<Entity> entityList = new ArrayList<>();
    
    // Located roughly 1km from (1,1)
    Entity testListingNear = new Entity("Listing");
    testListingNear.setProperty("name", "near");
    testListingNear.setProperty("latitude", 1.0063);
    testListingNear.setProperty("longitude", 1.0063);

    // Located roughly 10km from (1,1)
    Entity testListingFar = new Entity("Listing");
    testListingFar.setProperty("name", "far");        
    testListingFar.setProperty("latitude", 1.063);
    testListingFar.setProperty("longitude", 1.063);

    entityList.add(testListingNear);
    entityList.add(testListingFar);
    
    return entityList;
  }
}