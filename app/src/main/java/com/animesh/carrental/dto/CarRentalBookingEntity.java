package com.animesh.carrental.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarRentalBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String drivingLicenseNumber;
    private int age;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
    private Double rentalPrice;

    @Enumerated(EnumType.STRING)
    private CarSegment carSegment;

    public enum CarSegment {
        SMALL(String.valueOf("SMALL")),

        MEDIUM(String.valueOf("MEDIUM")),

        LARGE(String.valueOf("LARGE")),

        EXTRA_LARGE(String.valueOf("EXTRA_LARGE"));

        private final String value;

        CarSegment(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return String.valueOf(value);
        }

    }
}