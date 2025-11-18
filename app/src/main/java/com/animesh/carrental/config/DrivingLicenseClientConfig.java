package com.animesh.carrental.config;

import com.animesh.driving.license.client.api.DefaultApi;
import com.animesh.driving.license.client.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DrivingLicenseClientConfig {

    @Bean
    public ApiClient drivingLicenseApiClient(@Value("${driving-license.api.base-url}") String baseUrl) {
        ApiClient client = new ApiClient();
        client.setBasePath(baseUrl);
        return client;
    }

    @Bean
    public DefaultApi licensesApi(ApiClient drivingLicenseApiClient) {
        return new DefaultApi(drivingLicenseApiClient);
    }
}
