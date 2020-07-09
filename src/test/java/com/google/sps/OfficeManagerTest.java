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

@RunWith(JUnit4.class)
public final class OfficeManagerTest {
  private final double coordinateMarginError = 0.0000;

  @Test
  public void testSunnyvaleOffice() {
    Office svlOffice = OfficeManager.offices.get("svl");
    double svlLatitude = 37.4030;
    double svlLongitude = -122.0326;
        
    Assert.assertEquals("Sunnyvale", svlOffice.getName());
    Assert.assertEquals(svlLatitude, svlOffice.getLatitude(), coordinateMarginError);
    Assert.assertEquals(svlLongitude, svlOffice.getLongitude(), coordinateMarginError);
  }

  @Test
  public void testMountainViewOffice() {
    Office mtvOffice = OfficeManager.offices.get("mtv");
    double svlLatitude = 37.4220;
    double svlLongitude = -122.0841;
        
    Assert.assertEquals("Mountain View", mtvOffice.getName());
    Assert.assertEquals(svlLatitude, mtvOffice.getLatitude(), coordinateMarginError);
    Assert.assertEquals(svlLongitude, mtvOffice.getLongitude(), coordinateMarginError);
  }
}