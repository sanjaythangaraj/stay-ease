package com.example.stay_ease.exchange.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CustomerRequest {
  private String email;
  private String password;
  private String lastName;
  private String role;
}
