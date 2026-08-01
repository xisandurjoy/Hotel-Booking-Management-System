package com.sparktech.hotel.service;

import com.sparktech.hotel.entity.Booking;
import com.sparktech.hotel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Booking getById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void addBooking(Booking booking) {
        booking.setId(null);
        calculateDays(booking);
        bookingRepository.save(booking);
    }

    public void updateBooking(int id, Booking booking) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        booking.setId(existing.getId());
        calculateDays(booking);
        bookingRepository.save(booking);
    }

    public void deleteById(int id) {
        bookingRepository.deleteById(id);
    }

    // Days is always derived server-side from checkIn/checkOut - the form
    // never sends it, so it can never be entered incorrectly.
    private void calculateDays(Booking booking) {
        if (booking.getCheckIn() != null && booking.getCheckOut() != null) {
            long nights = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
            booking.setDays((int) Math.max(nights, 0));
        }
    }
}
