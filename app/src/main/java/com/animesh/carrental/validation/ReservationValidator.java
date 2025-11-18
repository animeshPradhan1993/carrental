package com.animesh.carrental.validation;

import com.animesh.carrental.dto.CarRentalBookingEntity;
import com.animesh.carrental.exception.InvalidBookingException;
import com.animesh.driving.license.client.model.LicenseResponse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Simple validator for reservation rules:
 * - Reservation length (inclusive) must be <= 30 days.
 * - Driving license must be present and at least 1 year old.
 */
@Component
@NoArgsConstructor
public final class ReservationValidator {

    private static final int MAX_DAYS = 30;
    private static final int MIN_LICENSE_YEARS = 1;

    /**
     * Validates a car rental booking against business rules:
     * <ul>
     *   <li>Start and end dates must be non-null.</li>
     *   <li>Start date must be on or before end date.</li>
     *   <li>Inclusive reservation length must not exceed 30 days.</li>
     *   <li>Driving license age validation pending (TODO).</li>
     * </ul>
     *
     * @param carRentalBookingEntity booking containing reservation dates (required)
     * @throws IllegalArgumentException if dates are null, inverted, or length exceeds 30 days
     */

    public void validateReservation(CarRentalBookingEntity carRentalBookingEntity, LicenseResponse licenseResponse) {
        var startDate = carRentalBookingEntity.getReservationStartDate();
        var endDate = carRentalBookingEntity.getReservationEndDate();

        Objects.requireNonNull(startDate, "start date must not be null");
        Objects.requireNonNull(endDate, "end date must not be null");

        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("start date can not be a past date");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidBookingException("start date must be on or before end date");
        }

        long daysInclusive = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (daysInclusive > MAX_DAYS) {
            throw new InvalidBookingException("reservation cannot exceed " + MAX_DAYS + " days (was " + daysInclusive + " days)");
        }

        var licenseValidity = licenseResponse.getExpiryDate();

        Objects.requireNonNull(licenseResponse, "A Valid license is required");
        Objects.requireNonNull(licenseResponse.getExpiryDate(), "A Valid license is required");

        LocalDate minAllowed = startDate.plusYears(MIN_LICENSE_YEARS);
        if (licenseValidity.isBefore(minAllowed)) {
            throw new InvalidBookingException("driving license must be at least " + MIN_LICENSE_YEARS + " year(s) valid after the start date");
        }
    }
}