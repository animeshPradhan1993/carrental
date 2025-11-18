package com.animesh.carrental.service;

import com.animesh.driving.license.client.api.DefaultApi;
import com.animesh.driving.license.client.model.LicenseRequest;
import com.animesh.driving.license.client.model.LicenseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LicenseService {
    private final DefaultApi licenseAPI;

    public LicenseResponse retriveLicense(LicenseRequest licenseRequest) {
        return licenseAPI.licenseDetailsPost(licenseRequest);
    }
}
