package com.example.stay_ease.controller;

import com.example.stay_ease.exchange.booking.BookingResponse;
import com.example.stay_ease.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

  private final BookingService bookingService;

  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @GetMapping
  public ResponseEntity<List<BookingResponse>> findAll() {
    List<BookingResponse> responses = bookingService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookingResponse> findById(@PathVariable Long id) {
    BookingResponse response = bookingService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BookingResponse> cancel(@PathVariable Long id) {
    bookingService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
