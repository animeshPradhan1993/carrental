package com.animesh.carrental.repository;

import com.animesh.carrental.dto.CarRentalBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRentalBookingRepository extends JpaRepository<CarRentalBookingEntity, Long> {
}