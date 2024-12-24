package com.example.stay_ease.service;

import com.example.stay_ease.data.BookingEntity;
import com.example.stay_ease.data.CustomerEntity;
import com.example.stay_ease.data.HotelEntity;
import com.example.stay_ease.exception.booking.BookingNotFoundException;
import com.example.stay_ease.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

  @Mock
  private BookingRepository bookingRepository;

  private ModelMapper modelMapper;

  @InjectMocks
  private BookingService bookingService;

  @BeforeEach
  void setup() {
    modelMapper = new ModelMapper();
    bookingService = new BookingService(bookingRepository, modelMapper);
  }

  @Test
  void testDeleteBookingSuccess() {
    Long bookingId = 1L;

    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setId(1L);

    HotelEntity hotelEntity = new HotelEntity();
    hotelEntity.setId(1L);

    BookingEntity bookingEntity = new BookingEntity();
    bookingEntity.setId(1L);
    bookingEntity.setHotelEntity(hotelEntity);
    bookingEntity.setCustomerEntity(customerEntity);

    Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));

    bookingService.delete(bookingId);

    Mockito.verify(bookingRepository).findById(bookingId);
    Mockito.verify(bookingRepository).deleteById(bookingId);

  }

  @Test
  void testDeleteBookingForBookingNotFound() {
    Long bookingId = 1L;

    assertThrows(BookingNotFoundException.class, () -> {
      bookingService.delete(bookingId);
    });

  }
}
