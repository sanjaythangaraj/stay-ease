package com.example.stay_ease.service;

import com.example.stay_ease.data.BookingEntity;
import com.example.stay_ease.data.CustomerEntity;
import com.example.stay_ease.data.HotelEntity;
import com.example.stay_ease.exception.hotel.HotelNotFoundException;
import com.example.stay_ease.exception.hotel.RoomNotAvailableForHotelException;
import com.example.stay_ease.exchange.booking.BookingResponse;
import com.example.stay_ease.exchange.hotel.CreateHotelRequest;
import com.example.stay_ease.exchange.hotel.HotelResponse;
import com.example.stay_ease.exchange.hotel.UpdateHotelRequest;
import com.example.stay_ease.repository.BookingRepository;
import com.example.stay_ease.repository.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

  private final HotelRepository hotelRepository;
  private final BookingRepository bookingRepository;
  private final CustomerService customerService;
  private final ModelMapper modelMapper;

  public HotelService(HotelRepository hotelRepository, BookingRepository bookingRepository, CustomerService customerService, ModelMapper modelMapper) {
    this.hotelRepository = hotelRepository;
    this.bookingRepository = bookingRepository;
    this.customerService = customerService;
    this.modelMapper = modelMapper;
  }

  public List<HotelResponse> findAll() {
    return hotelRepository.findAll().stream()
        .map(hotelEntity -> modelMapper.map(hotelEntity, HotelResponse.class))
        .toList();
  }

  private HotelEntity getHotelEntity(Long id) {
    return hotelRepository.findById(id).orElseThrow(HotelNotFoundException::new);
  }

  public HotelResponse findById(Long id) {
    return modelMapper.map(getHotelEntity(id), HotelResponse.class);
  }

  public HotelResponse save(CreateHotelRequest createHotelRequest) {
    HotelEntity hotelEntity = modelMapper.map(createHotelRequest, HotelEntity.class);
    hotelEntity = hotelRepository.save(hotelEntity);
    return modelMapper.map(hotelEntity, HotelResponse.class);
  }

  public HotelResponse update(Long id, UpdateHotelRequest updateHotelRequest) {
    HotelEntity hotelEntity = hotelRepository.save(mapToHotelEntity(id, updateHotelRequest));
    return modelMapper.map(hotelEntity, HotelResponse.class);
  }

  public void delete(Long id) {
    findById(id);
    hotelRepository.deleteById(id);
  }

  public BookingResponse bookRoom(Long id) {
    HotelEntity hotelEntity = hotelRepository.findById(id).orElseThrow(HotelNotFoundException::new);
    if (hotelEntity.getAvailableRooms() == 0) throw new RoomNotAvailableForHotelException();

    hotelEntity.setAvailableRooms(hotelEntity.getAvailableRooms() - 1);

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    CustomerEntity customerEntity = modelMapper.map(customerService.findOneByEmail(email), CustomerEntity.class);

    hotelRepository.save(hotelEntity);

    BookingEntity bookingEntity = new BookingEntity();
    bookingEntity.setHotelEntity(hotelEntity);
    bookingEntity.setCustomerEntity(customerEntity);

    bookingEntity = bookingRepository.save(bookingEntity);
    return modelMapper.map(bookingEntity, BookingResponse.class);
  }

  public List<BookingResponse> getBooking(Long id) {
    return getHotelEntity(id).getBookingEntityList().stream()
        .map(bookingEntity -> modelMapper.map(bookingEntity, BookingResponse.class)).toList();
  }

  private HotelEntity mapToHotelEntity(Long id, UpdateHotelRequest updateHotelRequest) {
    HotelEntity hotelEntity = modelMapper.map(findById(id), HotelEntity.class);

    if (updateHotelRequest.getName() != null) hotelEntity.setName(updateHotelRequest.getName());
    if (updateHotelRequest.getDescription() != null) hotelEntity.setDescription(updateHotelRequest.getDescription());
    if (updateHotelRequest.getLocation() != null) hotelEntity.setLocation(updateHotelRequest.getLocation());
    if (updateHotelRequest.getAvailableRooms() != null) hotelEntity.setAvailableRooms(updateHotelRequest.getAvailableRooms());

    return hotelEntity;

  }
}
