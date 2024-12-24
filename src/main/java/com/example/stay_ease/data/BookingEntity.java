package com.example.stay_ease.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BookingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "hotel_id")
  private HotelEntity hotelEntity;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private CustomerEntity customerEntity;
}
