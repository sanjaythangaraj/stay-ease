package com.example.stay_ease.repository;

import com.example.stay_ease.data.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

}
