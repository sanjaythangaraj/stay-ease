package com.example.stay_ease.repository;

import com.example.stay_ease.data.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
  List<HotelEntity> findAllByAvailableRoomsGreaterThan(int number);
}
