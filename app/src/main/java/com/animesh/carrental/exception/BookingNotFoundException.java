package com.animesh.carrental.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(Long id) {
        super("Booking not found with id: " + id);
    }
}