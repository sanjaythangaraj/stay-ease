package com.example.stay_ease.repository;

import com.example.stay_ease.data.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
  Optional<CustomerEntity> findOneByEmail(String email);
}
