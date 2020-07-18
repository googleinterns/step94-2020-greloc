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

import com.google.gson.Gson;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResult;
import com.google.sps.data.util.CategoryGroup;
import com.google.sps.data.util.GmapsHelper;
import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public final class GmapsHelperTest {

  private GmapsHelper gmaps;

  @Before
  public void setUp() {
    gmaps = new GmapsHelper(false);
  }

  @After
  public void tearDown() {}

  @Test
  public void testGetCategoryGroupByIdWithValidIds() {
    CategoryGroup recreation = gmaps.getCategoryGroupById(2);
    Assert.assertEquals(CategoryGroup.RECREATION, recreation);

    CategoryGroup dining = gmaps.getCategoryGroupById(3);
    Assert.assertEquals(CategoryGroup.DINING, dining);

    CategoryGroup grocery = gmaps.getCategoryGroupById(4);
    Assert.assertEquals(CategoryGroup.GROCERY, grocery);
  }

  @Test
  public void testGetCategoryGroupByIdWithInvalid() {
    CategoryGroup invalidBelowRange = gmaps.getCategoryGroupById(0);
    Assert.assertEquals(CategoryGroup.GROCERY, invalidBelowRange);

    CategoryGroup invalidAboveRange = gmaps.getCategoryGroupById(5);
    Assert.assertEquals(CategoryGroup.GROCERY, invalidAboveRange);
  }

  @Test
  public void testSearchNearbyCategoryGroupReturnsValidJSON()
      throws ApiException, InterruptedException, IOException {
    GmapsHelper gmapsSpy = Mockito.spy(gmaps);
    PlacesSearchResult[] emptyResult = {new PlacesSearchResult()};
    Mockito.doReturn(emptyResult)
        .when(gmapsSpy)
        .nearbySearchByPlaceType(
            ArgumentMatchers.any(PlaceType.class),
            ArgumentMatchers.anyDouble(),
            ArgumentMatchers.anyDouble(),
            ArgumentMatchers.anyInt());

    String results =
        gmapsSpy.searchNearbyCategoryGroup(CategoryGroup.GROCERY, 37.4030, -122.0326, 5000);
    Assert.assertEquals(true, isJSONValid(results));
  }

  private boolean isJSONValid(String jsonInString) {
    Gson gson = new Gson();
    try {
      gson.fromJson(jsonInString, Object.class);
      return true;
    } catch (com.google.gson.JsonSyntaxException ex) {
      return false;
    }
  }

  private void runTestCode() {
    GmapsHelper gmaps = new GmapsHelper(false);
    try {
      String results =
          gmaps.searchNearbyCategoryGroup(CategoryGroup.GROCERY, 37.4030, -122.0326, 5000);
      System.out.println(results);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
