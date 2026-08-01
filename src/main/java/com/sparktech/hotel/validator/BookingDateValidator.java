package com.sparktech.hotel.validator;

import com.sparktech.hotel.entity.Booking;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

// Single Responsibility: BookingController's only job is handling HTTP
// requests/responses. Business validation (check-out must be after
// check-in) lives here instead, so it can be reused, tested, and changed
// independently of the controller.
@Component
public class BookingDateValidator {

    public void validate(Booking booking, BindingResult bindingResult) {
        if (booking.getCheckIn() != null && booking.getCheckOut() != null
                && !booking.getCheckOut().isAfter(booking.getCheckIn())) {
            bindingResult.rejectValue("checkOut", "checkOut.invalid", "Check-out must be after check-in");
        }
    }
}
