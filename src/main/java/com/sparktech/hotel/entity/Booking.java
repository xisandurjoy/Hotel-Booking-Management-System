package com.sparktech.hotel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {

    // Auto-generated - never accepted from the form.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Guest guest;

    @NotNull(message = "Floor is required")
    @Min(value = 0, message = "Floor can't be negative")
    private Integer floor;

    @NotNull(message = "Room number is required")
    @Min(value = 1, message = "Room number must be 1 or higher")
    private Integer roomNumber;

    @NotNull(message = "Check-in date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkIn;

    @NotNull(message = "Check-out date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOut;

    // Auto counted in BookingService from checkIn/checkOut - the form never
    // sends this value, so it can't be tampered with.
    private Integer days;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be more than 0")
    private Double amount;

    @NotBlank(message = "Payment status is required")
    private String paymentStatus;
}
