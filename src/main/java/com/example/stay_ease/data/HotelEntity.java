package com.example.stay_ease.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class HotelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String location;
  private String description;
  private int availableRooms;

  @OneToMany(mappedBy = "hotelEntity")
  private List<BookingEntity> bookingEntityList = new ArrayList<>();
}
