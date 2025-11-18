package com.animesh.carrental.controller;

import com.animesh.car.rental.app.model.CarRentalBooking;
import com.animesh.carrental.exception.BookingNotFoundException;
import com.animesh.carrental.mapper.CarRentalBookingMapperImpl;
import com.animesh.carrental.service.CarRentalBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/bookings")
public class CarRentalBookingController {

    private final CarRentalBookingService service;
    private final CarRentalBookingMapperImpl mapper;

    @PostMapping("/confirm")
    public ResponseEntity<CarRentalBooking> confirmBooking(@RequestBody CarRentalBooking booking) {
        CarRentalBooking confirmedBooking = mapper.toModel(service.confirmBooking(mapper.toEntity(booking)));
        return ResponseEntity.ok(confirmedBooking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarRentalBooking> getBookingDetails(@PathVariable Long id) {

        return service.getBookingDetails(id).map(mapper::toModel).map(ResponseEntity::ok)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }
}