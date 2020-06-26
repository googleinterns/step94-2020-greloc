package com.google.sps.data;
import java.util.List;
import com.google.appengine.api.datastore.EmbeddedEntity;

public final class Listing {
  String ownerId;
  String listingId;
  String name;
  float price;
  String description;
  List<String> images;
  EmbeddedEntity contactInfo;
  boolean googlerOwned;
  long startTimestamp;
  long endTimestamp;
  long createdTimestamp;

  public String getId() {
    return this.listingId;
  }

  public String getOwnerId() {
    return this.ownerId;
  }

  public String getName() {
    return this.name;
  }

  public float getPrice() {
    return this.price;
  }

  public String getDesc() {
    return this.description;
  }

  public List<String> getImages() {
    return this.images;
  }

  public EmbeddedEntity getOwnerContactInfo() {
    return this.contactInfo;
  }

  public boolean isOwnedByGoogler() {
    return this.googlerOwned;
  }

  public long getStartTimestamp() {
    return this.startTimestamp;
  }
  
  public long getEndTimestamp() {
    return this.endTimestamp;
  }

  public long getCreatedTimestamp() {
    return this.createdTimestamp;
  }
}