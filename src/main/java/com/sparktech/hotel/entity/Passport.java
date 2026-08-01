package com.sparktech.hotel.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// @Embeddable = not its own table/entity. Its fields are stored as extra
// columns directly on whichever entity embeds it (here: Guest) - same as
// the Address example embedded inside Student. This just groups the
// passport fields into one reusable Java object instead of three loose
// fields scattered on Guest.
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passport {

    @NotBlank(message = "Passport number is required")
    @Size(max = 30, message = "Passport number looks too long")
    private String passportNumber;

    @NotNull(message = "Passport expiry date is required")
    @Future(message = "Passport expiry date must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expiryDate;

    @NotBlank(message = "Passport country is required")
    private String country;
}
