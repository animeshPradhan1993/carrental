package com.animesh.carrental.test.integration;


import com.animesh.car.rental.app.model.CarRentalBooking;
import com.animesh.car.rental.pricing.client.api.DefaultApi;
import com.animesh.car.rental.pricing.client.model.RateResponse;
import com.animesh.carrental.controller.CarRentalBookingController;
import com.animesh.carrental.exception.InvalidBookingException;
import com.animesh.carrental.mapper.CarRentalBookingMapperImpl;
import com.animesh.driving.license.client.model.LicenseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class End2EndTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CarRentalBookingController controller;
    @MockitoBean
    DefaultApi rentalApi;
    @MockitoBean
    com.animesh.driving.license.client.api.DefaultApi licenseAPI;
    @Autowired
    CarRentalBookingMapperImpl mapper;

    private CarRentalBooking createBooking(int age, LocalDate startDate, LocalDate endDate, CarRentalBooking.CarSegmentEnum segment, String license) {
        return new CarRentalBooking()
                .age(age)
                .carSegment(segment)
                .reservationStartDate(startDate)
                .reservationEndDate(endDate)
                .drivingLicenseNumber(license);
    }

    @Test
    public void testConfirmBooking() {
        Mockito.when(licenseAPI.licenseDetailsPost(Mockito.any())).thenReturn(new LicenseResponse().expiryDate(LocalDate.now().plusYears(2)));

        Mockito.when(rentalApi.rentalRatePost(Mockito.any())).thenReturn(new RateResponse().ratePerDay(50f));
        CarRentalBooking booking = createBooking(18, LocalDate.now(), LocalDate.now().plusDays(12), CarRentalBooking.CarSegmentEnum.EXTRA_LARGE, "1234");

        CarRentalBooking bookingConfirmed = controller.confirmBooking(booking).getBody();
        Assertions.assertEquals(600d, bookingConfirmed.getRentalPrice());

    }

    @Test
    public void failedBookingInvalidLicense() {
        Mockito.when(licenseAPI.licenseDetailsPost(Mockito.any())).thenReturn(new LicenseResponse().expiryDate(LocalDate.now().plusDays(300)));

        Mockito.when(rentalApi.rentalRatePost(Mockito.any())).thenReturn(new RateResponse().ratePerDay(50f));
        CarRentalBooking booking = createBooking(18, LocalDate.now(), LocalDate.now().plusDays(12), CarRentalBooking.CarSegmentEnum.EXTRA_LARGE, "1234");


        InvalidBookingException exception = Assertions.assertThrows(
                InvalidBookingException.class,
                () -> controller.confirmBooking(booking)
        );

        Assertions.assertEquals(
                "driving license must be at least 1 year(s) valid after the start date",
                exception.getMessage()
        );
    }

    @Test
    void failedBookingInvalidLicenseWIthMockMvc() throws Exception {
        when(licenseAPI.licenseDetailsPost(any()))
                .thenReturn(new LicenseResponse().expiryDate(LocalDate.now().plusDays(300)));

        when(rentalApi.rentalRatePost(any()))
                .thenReturn(new RateResponse().ratePerDay(50f));

        CarRentalBooking booking = createBooking(18, LocalDate.now(), LocalDate.now().plusDays(12), CarRentalBooking.CarSegmentEnum.EXTRA_LARGE, "1234");


        mockMvc.perform(post("/api/bookings/confirm")
                        .with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("driving license must be at least 1 year(s) valid after the start date"));
    }

    @Test
    void failedBookingInvalidEndDate() throws Exception {
        when(licenseAPI.licenseDetailsPost(any()))
                .thenReturn(new LicenseResponse().expiryDate(LocalDate.now().plusDays(300)));

        when(rentalApi.rentalRatePost(any()))
                .thenReturn(new RateResponse().ratePerDay(50f));

        CarRentalBooking booking = createBooking(18, LocalDate.now(), LocalDate.now().plusDays(-1), CarRentalBooking.CarSegmentEnum.EXTRA_LARGE, "1234");


        mockMvc.perform(post("/api/bookings/confirm")
                        .with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("start date must be on or before end date"));
    }

    @Test
    void failedBookingInvalidStartDate() throws Exception {
        when(licenseAPI.licenseDetailsPost(any()))
                .thenReturn(new LicenseResponse().expiryDate(LocalDate.now().plusDays(300)));

        when(rentalApi.rentalRatePost(any()))
                .thenReturn(new RateResponse().ratePerDay(50f));

        CarRentalBooking booking = createBooking(18, LocalDate.now().plusDays(-1), LocalDate.now(), CarRentalBooking.CarSegmentEnum.EXTRA_LARGE, "1234");


        mockMvc.perform(post("/api/bookings/confirm")
                        .with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("start date can not be a past date"));
    }

    @Test
    void failedBookingInvalidDates() throws Exception {
        when(licenseAPI.licenseDetailsPost(any()))
                .thenReturn(new LicenseResponse().expiryDate(LocalDate.now().plusDays(300)));

        when(rentalApi.rentalRatePost(any()))
                .thenReturn(new RateResponse().ratePerDay(50f));

        CarRentalBooking booking = createBooking(18, LocalDate.now(), LocalDate.now().plusDays(31), CarRentalBooking.CarSegmentEnum.EXTRA_LARGE, "1234");


        mockMvc.perform(post("/api/bookings/confirm")
                        .with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("reservation cannot exceed 30 days (was 32 days)"));
    }

}


