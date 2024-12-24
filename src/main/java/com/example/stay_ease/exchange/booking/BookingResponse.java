package com.example.stay_ease.exchange.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class BookingResponse {
  private Long id;
  private Long hotelId;
  private Long customerId;
}
