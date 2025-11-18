package com.animesh.carrental.test;

import com.animesh.carrental.config.SecurityConfig;
import com.animesh.carrental.controller.CarRentalBookingController;
import com.animesh.carrental.dto.CarRentalBookingEntity;
import com.animesh.carrental.mapper.CarRentalBookingMapperImpl;
import com.animesh.carrental.service.CarRentalBookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarRentalBookingController.class)
@Import({SecurityConfig.class, CarRentalBookingMapperImpl.class})
class CarRentalBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarRentalBookingService service;

    private CarRentalBookingEntity booking;

    @MockitoBean
    private CarRentalBookingMapperImpl mapper;

    @BeforeEach
    void setUp() {
        booking = new CarRentalBookingEntity();
        booking.setId(1L);
        booking.setDrivingLicenseNumber("DL123456");
        booking.setCarSegment(CarRentalBookingEntity.CarSegment.SMALL);
        booking.setReservationStartDate(LocalDate.now().plusDays(25));
        booking.setReservationEndDate(LocalDate.now().plusDays(30));
        booking.setRentalPrice(50d);
    }



    @Test
    void getBookingDetails_shouldReturnNotFoundWhenBookingDoesNotExist() throws Exception {
        // Given
        when(service.getBookingDetails(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/bookings/999").with(httpBasic("admin", "password")))
                .andExpect(status().isNotFound());

        verify(service).getBookingDetails(999L);
    }



    @Test
    void confirmBooking_shouldReturnOkWithBasicAuth() throws Exception {
        when(service.confirmBooking(any(CarRentalBookingEntity.class))).thenReturn(booking);

        mockMvc.perform(post("/api/bookings/confirm")
                        .with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk());
    }

    @Test
    void confirmBooking_shouldReturnUnauthorizedWithoutAuth() throws Exception {
        mockMvc.perform(post("/api/bookings/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isUnauthorized());
    }
}
