package com.example.stay_ease.service;

import com.example.stay_ease.data.BookingEntity;
import com.example.stay_ease.data.HotelEntity;
import com.example.stay_ease.exception.hotel.HotelNotFoundException;
import com.example.stay_ease.exception.hotel.RoomNotAvailableForHotelException;
import com.example.stay_ease.exchange.booking.BookingResponse;
import com.example.stay_ease.exchange.customer.CustomerResponse;
import com.example.stay_ease.repository.BookingRepository;
import com.example.stay_ease.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

  @Mock
  private HotelRepository hotelRepository;

  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private CustomerService customerService;

  private ModelMapper modelMapper;

  @InjectMocks
  private HotelService hotelService;

  @BeforeEach
  void setup() {
    modelMapper = new ModelMapper();
    hotelService = new HotelService(hotelRepository, bookingRepository, customerService, modelMapper);
  }

  @Test
  void testHotelBookingSuccess() {
    Long hotelId = 1L;

    Long customerId = 1L;
    String email = "test@example.com";

    HotelEntity hotelEntity = new HotelEntity();
    hotelEntity.setId(hotelId);
    hotelEntity.setAvailableRooms(2);

    CustomerResponse customerResponse = new CustomerResponse();
    customerResponse.setId(customerId);
    customerResponse.setEmail(email);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    Mockito.when(customerService.findOneByEmail(email)).thenReturn(customerResponse);
    Mockito.when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));
    Mockito.when(hotelRepository.save(Mockito.any(HotelEntity.class))).thenReturn(hotelEntity);
    Mockito.when(bookingRepository.save(Mockito.any(BookingEntity.class)))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, BookingEntity.class));

    BookingResponse response = hotelService.bookRoom(hotelId);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(hotelId, response.getHotelId());
    Assertions.assertEquals(customerId, response.getCustomerId());
    Assertions.assertEquals(1, hotelEntity.getAvailableRooms());

  }


  @Test
  void testHotelBookingForRoomNotAvailable() {
    Long hotelId = 1L;

    HotelEntity hotelEntity = new HotelEntity();
    hotelEntity.setId(hotelId);
    hotelEntity.setAvailableRooms(0);

    Mockito.when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));

    assertThrows(RoomNotAvailableForHotelException.class, () -> {
      hotelService.bookRoom(hotelId);
    });

  }

  @Test
  void testHotelBookingForHotelNotFound() {
    Long hotelId = 1L;

    assertThrows(HotelNotFoundException.class, () -> {
      hotelService.bookRoom(hotelId);
    });

  }

}
