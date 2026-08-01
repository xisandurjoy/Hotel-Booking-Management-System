package com.sparktech.hotel.controller;

import com.sparktech.hotel.entity.Booking;
import com.sparktech.hotel.service.BookingService;
import com.sparktech.hotel.service.GuestService;
import com.sparktech.hotel.util.AuthCheck;
import com.sparktech.hotel.validator.BookingDateValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;
    private final GuestService guestService;
    private final BookingDateValidator bookingDateValidator;

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("bookings", bookingService.getAll());
        return "booking/list";
    }

    // =====================================================================
    // ADD - completely separate pair of methods from EDIT below.
    // =====================================================================

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("booking", new Booking());
        model.addAttribute("guests", guestService.getAll());
        model.addAttribute("editMode", false);
        return "booking/form";
    }

    @PostMapping("/add")
    public String add(HttpSession session,
                       @RequestParam(value = "guestId", required = false) Integer guestId,
                       @Valid @ModelAttribute("booking") Booking booking, BindingResult bindingResult, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }

        // Plain lookup instead of a Spring Converter<String, Guest> bean -
        // the form just posts a plain id, we fetch the Guest ourselves.
        if (guestId != null) {
            booking.setGuest(guestService.getById(guestId));
        }
        if (booking.getGuest() == null) {
            bindingResult.rejectValue("guest", "guest.required", "Guest is required");
        }
        bookingDateValidator.validate(booking, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("guests", guestService.getAll());
            model.addAttribute("editMode", false);
            return "booking/form";
        }
        log.info("Adding new booking {}", booking);
        bookingService.addBooking(booking);
        return "redirect:/booking/list";
    }

    // =====================================================================
    // EDIT - id always comes from the path variable, never from the form.
    // =====================================================================

    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session, @PathVariable int id, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        Booking booking = bookingService.getById(id);
        if (booking == null) {
            return "redirect:/booking/list";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("guests", guestService.getAll());
        model.addAttribute("editMode", true);
        return "booking/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(HttpSession session, @PathVariable int id,
                        @RequestParam(value = "guestId", required = false) Integer guestId,
                        @Valid @ModelAttribute("booking") Booking booking,
                        BindingResult bindingResult, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }

        if (guestId != null) {
            booking.setGuest(guestService.getById(guestId));
        }
        if (booking.getGuest() == null) {
            bindingResult.rejectValue("guest", "guest.required", "Guest is required");
        }
        bookingDateValidator.validate(booking, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("guests", guestService.getAll());
            model.addAttribute("editMode", true);
            return "booking/form";
        }
        log.info("Updating booking id {} with {}", id, booking);
        bookingService.updateBooking(id, booking);
        return "redirect:/booking/list";
    }

    // =====================================================================
    // DELETE
    // =====================================================================

    @GetMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable int id) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        log.info("Deleting booking with id {}", id);
        bookingService.deleteById(id);
        return "redirect:/booking/list";
    }
}
