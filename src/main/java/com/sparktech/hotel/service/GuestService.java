package com.sparktech.hotel.service;

import com.sparktech.hotel.entity.Guest;
import com.sparktech.hotel.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    public List<Guest> getAll() {
        return guestRepository.findAll();
    }

    public Guest getById(int id) {
        return guestRepository.findById(id).orElse(null);
    }

    public void addGuest(Guest guest) {
        guest.setId(null); // guarantee a brand-new row, never overwrite an existing one
        guestRepository.save(guest);
    }

    public void updateGuest(int id, Guest guest) {
        Guest existing = guestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found with id: " + id));

        guest.setId(existing.getId());
        guestRepository.save(guest);
    }

    public void deleteById(int id) {
        guestRepository.deleteById(id);
    }

    // Fixed checkbox choices for the special-requests picker on the guest form.
    public List<String> getSpecialRequestOptions() {
        return List.of("Sea View", "Extra Bed", "Late Checkout",
                "Early Check-in", "Airport Pickup", "Non-Smoking Room");
    }
}
