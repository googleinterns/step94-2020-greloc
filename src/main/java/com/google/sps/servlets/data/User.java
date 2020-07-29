package com.google.sps.data;

public class User {
  UserType type;
  String email;

  public User(UserType type, String email) {
    this.type = type;
    this.email = email;
  }

  public UserType getType() {
    return this.type;
  }

  public String getEmail() {
    return this.email;
  }
}
