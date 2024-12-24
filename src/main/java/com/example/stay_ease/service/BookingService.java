package com.example.stay_ease.service;

import com.example.stay_ease.exception.booking.BookingNotFoundException;
import com.example.stay_ease.exchange.booking.BookingResponse;
import com.example.stay_ease.repository.BookingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

  private final BookingRepository bookingRepository;
  private final ModelMapper modelMapper;

  public BookingService(BookingRepository bookingRepository, ModelMapper modelMapper) {
    this.bookingRepository = bookingRepository;
    this.modelMapper = modelMapper;
  }

  public BookingResponse findById(Long id) {
    return bookingRepository.findById(id).map(bookingEntity -> modelMapper.map(bookingEntity, BookingResponse.class))
        .orElseThrow(BookingNotFoundException::new);
  }

  public List<BookingResponse> findAll() {
    return bookingRepository.findAll().stream()
        .map(bookingEntity -> modelMapper.map(bookingEntity, BookingResponse.class)).toList();
  }

  public void delete(Long id) {
    findById(id);
    bookingRepository.deleteById(id);
  }


}
