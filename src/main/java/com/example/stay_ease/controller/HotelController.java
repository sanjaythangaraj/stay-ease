package com.example.stay_ease.controller;

import com.example.stay_ease.exchange.booking.BookingResponse;
import com.example.stay_ease.exchange.hotel.CreateHotelRequest;
import com.example.stay_ease.exchange.hotel.HotelResponse;
import com.example.stay_ease.exchange.hotel.UpdateHotelRequest;
import com.example.stay_ease.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

  private final HotelService hotelService;

  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  public ResponseEntity<List<HotelResponse>> findAll() {
    List<HotelResponse> responses = hotelService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<HotelResponse> findById(@PathVariable Long id) {
    HotelResponse response = hotelService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping
  public ResponseEntity<HotelResponse> create(@RequestBody @Valid CreateHotelRequest createHotelRequest) {
    HotelResponse response = hotelService.save(createHotelRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<HotelResponse> update(
      @PathVariable Long id,
      @RequestBody @Valid UpdateHotelRequest updateHotelRequest) {
    HotelResponse response = hotelService.update(id, updateHotelRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HotelResponse> delete(@PathVariable Long id) {
    hotelService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/book")
  public ResponseEntity<BookingResponse> book(@PathVariable Long id) {
    BookingResponse response = hotelService.bookRoom(id);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}/bookings")
  public ResponseEntity<List<BookingResponse>> bookings(@PathVariable Long id) {
    List<BookingResponse> responses = hotelService.getBooking(id);
    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
