package com.google.sps.data;

public enum EntityType {
  LISTING("Listing"),
  BUS_STOP("BusStop");

  private final String value;

  private EntityType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
