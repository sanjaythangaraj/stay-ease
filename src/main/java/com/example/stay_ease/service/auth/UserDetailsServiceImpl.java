package com.example.stay_ease.service.auth;

import com.example.stay_ease.data.CustomerEntity;
import com.example.stay_ease.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  final CustomerRepository customerRepository;

  public UserDetailsServiceImpl(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<CustomerEntity> optional = customerRepository.findOneByEmail(username);

    CustomerEntity customerEntity = optional
        .orElseThrow(() ->
            new UsernameNotFoundException("User details not found for the given email: " + username));
    String password = customerEntity.getPassword();
    List<GrantedAuthority> grantedAuthorityList = Set.of(customerEntity.getAuthorityEntity())
        .stream()
        .map(authorityEntity ->
            (GrantedAuthority) new SimpleGrantedAuthority(authorityEntity.getName()))
        .toList();

    return new User(username, password, grantedAuthorityList);
  }
}
