package com.example.stay_ease.exception.hotel;

public class RoomNotAvailableForHotelException extends RuntimeException {
  public RoomNotAvailableForHotelException() {
    super("Room not available in hotel!");
  }

  public RoomNotAvailableForHotelException(String message) {
    super(message);
  }
}
