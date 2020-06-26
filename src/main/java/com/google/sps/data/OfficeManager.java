package com.google.sps.data;
import java.util.HashMap;

public final class OfficeManager {
  public static final HashMap<String, Office> offices = new HashMap<String, Office>() {{
    put("svl", new Office("Sunnyvale", 37.4030, -122.0326));
    put("mtv", new Office("Sunnyvale", 37.4220, -122.0841));
  }};
}