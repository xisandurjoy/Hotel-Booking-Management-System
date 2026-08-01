package com.sparktech.hotel.repository;

import com.sparktech.hotel.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Integer> {
}
