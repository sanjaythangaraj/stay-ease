package com.example.stay_ease.exchange.hotel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class HotelResponse {

  private Long id;
  private String name;
  private String location;
  private String description;
  private Integer availableRooms;
}
