package com.animesh.carrental.service;

import com.animesh.car.rental.pricing.client.model.RateRequest;
import com.animesh.car.rental.pricing.client.model.RateResponse;
import com.animesh.carrental.dto.CarRentalBookingEntity;
import com.animesh.carrental.repository.CarRentalBookingRepository;
import com.animesh.carrental.validation.ReservationValidator;
import com.animesh.driving.license.client.model.LicenseRequest;
import com.animesh.driving.license.client.model.LicenseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarRentalBookingService {

    private final CarRentalBookingRepository repository;
    private final ReservationValidator reservationValidator;
    private final LicenseService licenseService;
    private final CarRentalPricingService pricingService;


    public CarRentalBookingEntity confirmBooking(CarRentalBookingEntity booking) {
        LicenseResponse licenseResponse = licenseService.retriveLicense(new LicenseRequest().licenseNumber(booking.getDrivingLicenseNumber()));
        RateResponse rateResponse = pricingService.retrievePrice(new RateRequest().category(RateRequest.CategoryEnum.valueOf(booking.getCarSegment().name())));
        reservationValidator.validateReservation(booking, licenseResponse);

        var completedBooking = repository.save(booking);
        completedBooking.setRentalPrice(0d + rateResponse.getRatePerDay() * ChronoUnit.DAYS.between(booking.getReservationStartDate(), booking.getReservationEndDate()));
        return completedBooking;
    }


    public Optional<CarRentalBookingEntity> getBookingDetails(Long id) {
        return repository.findById(id);
    }

}