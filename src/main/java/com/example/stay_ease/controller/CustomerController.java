package com.example.stay_ease.controller;

import com.example.stay_ease.exchange.booking.BookingResponse;
import com.example.stay_ease.exchange.customer.CustomerResponse;
import com.example.stay_ease.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping
  public ResponseEntity<List<CustomerResponse>> findAll() {
    List<CustomerResponse> responses = customerService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
    CustomerResponse response = customerService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{id}/bookings")
  public ResponseEntity<List<BookingResponse>> getBookings(@PathVariable Long id) {
    List<BookingResponse> list = customerService.getBookings(id);
    return ResponseEntity.status(HttpStatus.OK).body(list);
  }
}
