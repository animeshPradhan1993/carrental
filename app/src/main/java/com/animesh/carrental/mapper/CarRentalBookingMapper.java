package com.animesh.carrental.mapper;

import com.animesh.car.rental.app.model.CarRentalBooking;
import com.animesh.carrental.dto.CarRentalBookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarRentalBookingMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "carSegment", source = "carSegment")
    @Mapping(target = "drivingLicenseNumber", source = "drivingLicenseNumber")
    @Mapping(target = "reservationStartDate", source = "reservationStartDate")
    @Mapping(target = "reservationEndDate", source = "reservationEndDate")
    @Mapping(target = "rentalPrice", source = "rentalPrice")
    CarRentalBookingEntity toEntity(CarRentalBooking model);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "carSegment", source = "carSegment")
    @Mapping(target = "drivingLicenseNumber", source = "drivingLicenseNumber")
    @Mapping(target = "reservationStartDate", source = "reservationStartDate")
    @Mapping(target = "reservationEndDate", source = "reservationEndDate")
    @Mapping(target = "rentalPrice", source = "rentalPrice")
    CarRentalBooking toModel(CarRentalBookingEntity entity);
}
