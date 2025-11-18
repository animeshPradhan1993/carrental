package com.animesh.carrental.service;

import com.animesh.car.rental.pricing.client.api.DefaultApi;
import com.animesh.car.rental.pricing.client.model.RateRequest;
import com.animesh.car.rental.pricing.client.model.RateResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarRentalPricingService {
    private final DefaultApi api;

    public RateResponse retrievePrice(RateRequest request) {
        return api.rentalRatePost(request);
    }
}
