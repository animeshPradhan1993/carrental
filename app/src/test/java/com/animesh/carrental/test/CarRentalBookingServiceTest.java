package com.animesh.carrental.test;

import com.animesh.car.rental.pricing.client.model.RateRequest;
import com.animesh.car.rental.pricing.client.model.RateResponse;
import com.animesh.carrental.dto.CarRentalBookingEntity;
import com.animesh.carrental.dto.CarRentalBookingEntity.CarSegment;
import com.animesh.carrental.repository.CarRentalBookingRepository;
import com.animesh.carrental.service.CarRentalBookingService;
import com.animesh.carrental.service.CarRentalPricingService;
import com.animesh.carrental.service.LicenseService;
import com.animesh.carrental.validation.ReservationValidator;
import com.animesh.driving.license.client.model.LicenseRequest;
import com.animesh.driving.license.client.model.LicenseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarRentalBookingServiceTest {

    @Mock
    private CarRentalBookingRepository repository;

    @Mock
    private ReservationValidator reservationValidator;

    @Mock
    private LicenseService licenseService;

    @Mock
    private CarRentalPricingService pricingService;

    @InjectMocks
    private CarRentalBookingService carRentalBookingService;

    private CarRentalBookingEntity booking;

    private RateResponse rateResponse;

    @BeforeEach
    void setUp() {
        booking = new CarRentalBookingEntity();
        booking.setDrivingLicenseNumber("DL123456");
        booking.setCarSegment(CarSegment.SMALL);
        booking.setReservationStartDate(LocalDate.now());
        booking.setReservationEndDate(LocalDate.now().plusDays(3));


        rateResponse = new RateResponse();
        rateResponse.setRatePerDay(50f);
    }

    LicenseResponse createValidLicenseResponse() {

        return new LicenseResponse().expiryDate(LocalDate.now().plusYears(1));
    }

    LicenseResponse createInValidLicenseResponse() {

        return new LicenseResponse().expiryDate(LocalDate.now().plusDays(200));
    }

    @Test
    void confirmBooking_shouldSaveBookingSuccessfully() {
        // Given
        CarRentalBookingEntity savedBooking = new CarRentalBookingEntity();
        savedBooking.setId(1L);
        savedBooking.setDrivingLicenseNumber(booking.getDrivingLicenseNumber());
        savedBooking.setCarSegment(booking.getCarSegment());
        savedBooking.setReservationStartDate(booking.getReservationStartDate());
        savedBooking.setReservationEndDate(booking.getReservationEndDate());

        when(licenseService.retriveLicense(any(LicenseRequest.class))).thenReturn(createValidLicenseResponse());
        when(pricingService.retrievePrice(any(RateRequest.class))).thenReturn(rateResponse);
        when(repository.save(booking)).thenReturn(savedBooking);
        doNothing().when(reservationValidator).validateReservation(any(), any());

        // When
        CarRentalBookingEntity result = carRentalBookingService.confirmBooking(booking);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(licenseService).retriveLicense(any(LicenseRequest.class));
        verify(pricingService).retrievePrice(any(RateRequest.class));
        verify(reservationValidator).validateReservation(booking, createValidLicenseResponse());
        verify(repository).save(booking);
    }

    @Test
    void confirmBooking_shouldRetrieveLicenseWithCorrectLicenseNumber() {
        // Given
        when(licenseService.retriveLicense(any(LicenseRequest.class))).thenReturn(createValidLicenseResponse());
        when(pricingService.retrievePrice(any(RateRequest.class))).thenReturn(rateResponse);
        when(repository.save(any())).thenReturn(booking);

        // When
        carRentalBookingService.confirmBooking(booking);

        // Then
        verify(licenseService).retriveLicense(argThat(request ->
                request.getLicenseNumber().equals("DL123456")
        ));
    }

    @Test
    void confirmBooking_shouldRetrievePriceWithCorrectCarSegment() {
        // Given
        when(licenseService.retriveLicense(any(LicenseRequest.class))).thenReturn(createInValidLicenseResponse());
        when(pricingService.retrievePrice(any(RateRequest.class))).thenReturn(rateResponse);
        when(repository.save(any())).thenReturn(booking);

        // When
        carRentalBookingService.confirmBooking(booking);

        // Then
        verify(pricingService).retrievePrice(argThat(request ->
                request.getCategory().equals(RateRequest.CategoryEnum.SMALL)
        ));
    }

    @Test
    void confirmBooking_shouldCalculateRentalPriceCorrectly() {
        // Given
        CarRentalBookingEntity savedBooking = new CarRentalBookingEntity();
        savedBooking.setReservationStartDate(LocalDate.of(2024, 1, 1));
        savedBooking.setReservationEndDate(LocalDate.of(2024, 1, 4));

        when(licenseService.retriveLicense(any(LicenseRequest.class))).thenReturn(createValidLicenseResponse());
        when(pricingService.retrievePrice(any(RateRequest.class))).thenReturn(rateResponse);
        when(repository.save(any())).thenReturn(savedBooking);

        // When
        CarRentalBookingEntity result = carRentalBookingService.confirmBooking(booking);

        // Then
        //
    }
}