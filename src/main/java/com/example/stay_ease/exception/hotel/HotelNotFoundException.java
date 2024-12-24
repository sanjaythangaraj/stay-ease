package com.example.stay_ease.exception.hotel;

public class HotelNotFoundException extends RuntimeException {

  public HotelNotFoundException() {
    super("Hotel with given id not found!");
  }

  public HotelNotFoundException(String message) {
    super(message);
  }
}
