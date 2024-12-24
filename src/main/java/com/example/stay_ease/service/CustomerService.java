package com.example.stay_ease.service;

import com.example.stay_ease.data.AuthorityEntity;
import com.example.stay_ease.data.CustomerEntity;
import com.example.stay_ease.exception.customer.CustomerAlreadyExistsException;
import com.example.stay_ease.exception.customer.CustomerNotFoundException;
import com.example.stay_ease.exception.customer.UnauthorizedAccessException;
import com.example.stay_ease.exchange.booking.BookingResponse;
import com.example.stay_ease.exchange.customer.CustomerRequest;
import com.example.stay_ease.exchange.customer.CustomerResponse;
import com.example.stay_ease.repository.AuthorityRepository;
import com.example.stay_ease.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  private final AuthorityRepository authorityRepository;

  private final ModelMapper modelMapper;

  public CustomerService(CustomerRepository customerRepository, AuthorityRepository authorityRepository, ModelMapper modelMapper) {
    this.customerRepository = customerRepository;
    this.authorityRepository = authorityRepository;
    this.modelMapper = modelMapper;
  }

  public List<CustomerResponse> findAll() {
    return customerRepository.findAll().stream()
        .map(customerEntity -> modelMapper.map(customerEntity, CustomerResponse.class))
        .toList();
  }

  public CustomerResponse findById(Long id) {
    CustomerEntity customerEntity = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);

    Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    String role = authentication.getAuthorities().stream().toList().getFirst().getAuthority();
    if (!customerEntity.getEmail().equals(email) &&
        !role.equals("ROLE_ADMIN")) {
      throw new UnauthorizedAccessException();
    }
    return modelMapper.map(customerEntity, CustomerResponse.class);
  }

  public CustomerResponse save(CustomerRequest customerRequest) {
    if (customerRepository.findOneByEmail(customerRequest.getEmail()).isPresent()) {
      throw new CustomerAlreadyExistsException();
    }

    Optional<AuthorityEntity> authorityEntityOptional = authorityRepository.findByName(customerRequest.getRole());
    AuthorityEntity authorityEntity;
    if (authorityEntityOptional.isEmpty()) {
      authorityEntity = new AuthorityEntity();
      authorityEntity.setName(customerRequest.getRole());
      authorityEntity = authorityRepository.save(authorityEntity);
    } else {
      authorityEntity = authorityEntityOptional.get();
    }

    CustomerEntity customerEntity = modelMapper.map(customerRequest, CustomerEntity.class);
    customerEntity.setAuthorityEntity(authorityEntity);

    customerEntity = customerRepository.save(customerEntity);

    CustomerResponse customerResponse = modelMapper.map(customerEntity, CustomerResponse.class);
    customerResponse.setRole(customerRequest.getRole());
    return customerResponse;
  }

  public CustomerResponse findOneByEmail(String email) {
    return customerRepository.findOneByEmail(email)
        .map(customerEntity -> modelMapper.map(customerEntity, CustomerResponse.class))
        .orElseThrow(CustomerNotFoundException::new);
  }

  public List<BookingResponse> getBookings(Long id) {
    CustomerEntity customerEntity = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);

    Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    String role = authentication.getAuthorities().stream().toList().getFirst().getAuthority();
    if (!customerEntity.getEmail().equals(email) &&
        role.equals("ROLE_CUSTOMER")) {
      throw new UnauthorizedAccessException();
    }
    return customerEntity
        .getBookingEntityList().stream()
        .map(bookingEntity -> modelMapper.map(bookingEntity, BookingResponse.class))
        .toList();
  }


}
