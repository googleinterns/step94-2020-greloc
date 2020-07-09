package com.google.sps.data;

public class Office {
  String name;

  // Horizontal but shown as North/South
  double latitude;

  // Verical but shown as East/West
  double longitude;

  public Office(String name, double latitude, double longitude) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public String getName() {
    return this.name;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public double getLongitude() {
    return this.longitude;
  }
}
