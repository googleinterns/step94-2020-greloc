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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class QueryHelperTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private DatastoreService ds;
  private final Office testOffice = new Office("test", 1, 1);

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

  @Test
  public void testDistanceBasedEntitiesReturnsInRange() {
    insertNearListing();
    int distanceFromOfficeKilometers = 2;
    
    List<Entity> results = QueryHelper.getDistanceBasedEntities(EntityType.LISTING, 10, testOffice, distanceFromOfficeKilometers);
    Assert.assertEquals(1, results.size());

    String listingName = (String) results.get(0).getProperty("name");
    Assert.assertEquals("near", listingName);
  }

  @Test
  public void testDistanceBasedEntitiesDoesNotReturnOutOfRange() {
    insertFarListing();
    int distanceFromOfficeKilometers = 2;
    
    List<Entity> results = QueryHelper.getDistanceBasedEntities(EntityType.LISTING, 10, testOffice, distanceFromOfficeKilometers);
    Assert.assertEquals(0, results.size());
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
}