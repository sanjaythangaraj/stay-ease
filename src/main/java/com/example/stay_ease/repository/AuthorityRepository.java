package com.example.stay_ease.repository;

import com.example.stay_ease.data.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

  Optional<AuthorityEntity> findByName(String name);
}
