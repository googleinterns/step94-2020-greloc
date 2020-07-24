package com.google.sps.data;

import java.util.*; 

  public enum Type {
    RENTER(1),
    HOST(2),
    BOTH(3),
    UNKNOWN(4);

    private int value;
    private static Map map = new HashMap<>();

    private Type(int value) {
      this.value = value;
    }

    static {
      for(Type type : Type.values()) {
        map.put(type.value, type);
      }
    }

    public static Type valueOf(int type) {
      return (Type) map.get(type);

    }

    public int getValue() {
      return value;
    }
  }

