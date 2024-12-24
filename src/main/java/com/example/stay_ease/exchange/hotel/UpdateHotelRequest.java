package com.example.stay_ease.exchange.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class UpdateHotelRequest {
  private String name;
  private String location;
  private String description;
  private Integer availableRooms;
}
