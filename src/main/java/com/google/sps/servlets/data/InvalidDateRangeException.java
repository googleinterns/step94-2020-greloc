package com.google.sps.data;

public class InvalidDateRangeException extends Exception { 
  public InvalidDateRangeException(String errorMessage) {
    super(errorMessage);
  }
}