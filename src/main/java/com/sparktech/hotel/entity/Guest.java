package com.sparktech.hotel.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Guest {

    // Auto-generated - never accepted from the form, so it can never be
    // changed/spoofed by the user, whether adding or editing.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name can't be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2-100 characters")
    private String name;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9+\\-\\s]{7,15}$", message = "Enter a valid mobile number")
    private String mobile;

    // ---- Passport information, grouped as ONE embedded object instead of
    // three loose fields. Columns still live on the guest table (same as
    // before) - @Embedded just organizes them under guest.getPassport().
    // "@Valid" makes Spring cascade-validate the Passport's own annotations
    // (NotBlank/Future/etc.) whenever a Guest is validated. ----
    @Valid
    @Embedded
    private Passport passport = new Passport();

    // ---- Special requests, now a checkbox-picked list instead of free
    // text. @ElementCollection stores each selected item as its own row in
    // a separate "guest_special_requests" table (guest_id, request) - same
    // idea as the mobileNumbers example, just with an explicit table/column
    // name so it doesn't rely on Hibernate's default naming. ----
    @ElementCollection
    @CollectionTable(name = "guest_special_requests", joinColumns = @JoinColumn(name = "guest_id"))
    @Column(name = "request")
    private List<String> specialRequests = new ArrayList<>();

    // One guest -> many bookings. mappedBy = "guest" means Booking already
    // owns the foreign key (booking.guest_id), so this side is read-only
    // and does NOT create any extra join table/column.
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Booking> bookings = new ArrayList<>();
}
