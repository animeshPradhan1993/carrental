package com.animesh.carrental.test;

import com.animesh.car.rental.pricing.client.api.DefaultApi;
import com.animesh.car.rental.pricing.client.model.RateRequest;
import com.animesh.car.rental.pricing.client.model.RateResponse;
import com.animesh.carrental.service.CarRentalPricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarRentalPricingServiceTest {

    @Mock
    private DefaultApi api;

    @InjectMocks
    private CarRentalPricingService carRentalPricingService;

    private RateRequest rateRequest;
    private RateResponse rateResponse;

    @BeforeEach
    void setUp() {
        rateRequest = new RateRequest();
        rateRequest.setCategory(RateRequest.CategoryEnum.SMALL);

        rateResponse = new RateResponse();
        rateResponse.setRatePerDay(50f);
    }

    @Test
    void retrievePrice_shouldReturnRateResponse() {
        // Given
        when(api.rentalRatePost(rateRequest)).thenReturn(rateResponse);

        // When
        RateResponse result = carRentalPricingService.retrievePrice(rateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRatePerDay()).isEqualTo(50.0f);
        verify(api).rentalRatePost(rateRequest);
    }

    @Test
    void retrievePrice_shouldCallApiWithCorrectRequest() {
        // Given
        when(api.rentalRatePost(any(RateRequest.class))).thenReturn(rateResponse);

        // When
        carRentalPricingService.retrievePrice(rateRequest);

        // Then
        verify(api).rentalRatePost(rateRequest);
    }

    @Test
    void retrievePrice_shouldHandleDifferentCarCategories() {
        // Given
        rateRequest.setCategory(RateRequest.CategoryEnum.MEDIUM);
        rateResponse.setRatePerDay(75f);
        when(api.rentalRatePost(rateRequest)).thenReturn(rateResponse);

        // When
        RateResponse result = carRentalPricingService.retrievePrice(rateRequest);

        // Then
        assertThat(result.getRatePerDay()).isEqualTo(75.0f);
    }
}
