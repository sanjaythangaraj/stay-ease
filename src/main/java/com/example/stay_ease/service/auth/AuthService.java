package com.example.stay_ease.service.auth;

import com.example.stay_ease.exchange.auth.LoginRequest;
import com.example.stay_ease.exchange.auth.LoginResponse;
import com.example.stay_ease.exchange.auth.RegisterRequest;
import com.example.stay_ease.exchange.auth.RegisterResponse;
import com.example.stay_ease.exchange.customer.CustomerRequest;
import com.example.stay_ease.exchange.customer.CustomerResponse;
import com.example.stay_ease.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final CustomerService customerService;

  private final AuthenticationManager authenticationManager;

  private final JWTService jwtService;

  private final PasswordEncoder passwordEncoder;

  private final ModelMapper modelMapper;

  public AuthService(CustomerService customerService, AuthenticationManager authenticationManager, JWTService jwtService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
    this.customerService = customerService;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
    this.modelMapper = modelMapper;
  }

  public RegisterResponse register(RegisterRequest registerRequest) {
    CustomerRequest customerRequest = modelMapper.map(registerRequest, CustomerRequest.class);
    String hashPwd = passwordEncoder.encode(customerRequest.getPassword());
    customerRequest.setPassword(hashPwd);
    if (customerRequest.getRole() == null) customerRequest.setRole("customer");
    customerRequest.setRole("ROLE_" + customerRequest.getRole().toUpperCase());
    CustomerResponse customerResponse = customerService.save(customerRequest);
    return modelMapper.map(customerResponse, RegisterResponse.class);
  }

  private Authentication authenticate(String username, String password) {
    Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    return authenticationManager.authenticate(authentication);
  }

  private String authenticateAndGetJWT(String username, String password) {
    Authentication authentication = authenticate(username, password);
    if (authentication != null && authentication.isAuthenticated()) {
      return jwtService.getJWT(authentication);
    }
    return null;
  }

  public LoginResponse login(LoginRequest loginRequest) {
    String jwt = authenticateAndGetJWT(loginRequest.email(), loginRequest.password());
    if (jwt == null) jwt = "";
    return new LoginResponse(HttpStatus.OK.getReasonPhrase(), jwt);
  }

}
