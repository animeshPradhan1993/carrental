package com.animesh.carrental.test;

import com.animesh.carrental.service.LicenseService;
import com.animesh.driving.license.client.api.DefaultApi;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LicenseServiceTest {

    @Mock
    private DefaultApi licenseAPI;

    @InjectMocks
    private LicenseService licenseService;

    private LicenseRequest licenseRequest;
    private LicenseResponse licenseResponse;

    @BeforeEach
    void setUp() {
        licenseRequest = new LicenseRequest();
        licenseRequest.setLicenseNumber("DL123456");

        licenseResponse = new LicenseResponse();
        licenseResponse.setExpiryDate(LocalDate.now().plusYears(2));
    }

    @Test
    void retriveLicense_shouldReturnLicenseResponse() {
        // Given
        when(licenseAPI.licenseDetailsPost(licenseRequest)).thenReturn(licenseResponse);

        // When
        LicenseResponse result = licenseService.retriveLicense(licenseRequest);

        // Then
        assertThat(result).isNotNull();

        verify(licenseAPI).licenseDetailsPost(licenseRequest);
    }

    @Test
    void retriveLicense_shouldCallApiWithCorrectRequest() {
        // Given
        when(licenseAPI.licenseDetailsPost(any(LicenseRequest.class))).thenReturn(licenseResponse);

        // When
        licenseService.retriveLicense(licenseRequest);

        // Then
        verify(licenseAPI).licenseDetailsPost(licenseRequest);
    }

    @Test
    void retriveLicense_shouldHandleInvalidLicense() {
        // Given

        when(licenseAPI.licenseDetailsPost(licenseRequest)).thenReturn(licenseResponse);

        // When
        LicenseResponse result = licenseService.retriveLicense(licenseRequest);

        // Then
    }
}
