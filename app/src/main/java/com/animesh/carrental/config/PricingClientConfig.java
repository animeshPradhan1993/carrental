package com.animesh.carrental.config;

import com.animesh.car.rental.pricing.client.api.DefaultApi;
import com.animesh.car.rental.pricing.client.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PricingClientConfig {

    @Bean
    public ApiClient pricingApiClient(@Value("${pricing.api.base-url}") String baseUrl) {
        ApiClient client = new ApiClient();
        client.setBasePath(baseUrl);
        return client;
    }

    @Bean
    public DefaultApi ratesApi(ApiClient pricingApiClient) {
        return new DefaultApi(pricingApiClient);
    }
}
