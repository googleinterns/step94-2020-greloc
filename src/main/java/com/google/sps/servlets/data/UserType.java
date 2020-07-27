package com.google.sps.data;

import java.util.*; 

  public enum UserType {
    RENTER(1),
    HOST(2),
    BOTH(3),
    UNKNOWN(4);

    private int value;
    private static Map map = new HashMap<>();

    private UserType(int value) {
      this.value = value;
    }

    static {
      for(UserType Usertype : UserType.values()) {
        map.put(Usertype.value, Usertype);
      }
    }

    public static UserType valueOf(int Usertype) {
      return (UserType) map.get(Usertype);

    }

    public int getValue() {
      return value;
    }
  }

