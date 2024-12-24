package com.example.stay_ease.exchange.hotel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CreateHotelRequest {

  @NotBlank(message = "Name is mandatory")
  private String name;

  @NotBlank(message = "Location is mandatory")
  private String location;

  @NotBlank(message = "Description is mandatory")
  private String description;

  @NotNull(message = "Available Rooms is mandatory")
  private Integer availableRooms;
}
