package com.example.stay_ease.exception.booking;

public class BookingNotFoundException extends RuntimeException {
  public BookingNotFoundException() {
    super("Booking with given id not found!");
  }

  public BookingNotFoundException(String message) {
    super(message);
  }
}
