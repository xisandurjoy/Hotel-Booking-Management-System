package com.sparktech.hotel.controller;

import com.sparktech.hotel.entity.Guest;
import com.sparktech.hotel.service.GuestService;
import com.sparktech.hotel.util.AuthCheck;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/guest")
public class GuestController {

    private final GuestService guestService;

    // Runs before every handler below and adds the checkbox choices to the
    // model automatically, so add/edit - including their validation-error
    // return paths - always have it without repeating
    // model.addAttribute(...) in every method.
    @ModelAttribute("specialRequestOptions")
    public List<String> specialRequestOptions() {
        return guestService.getSpecialRequestOptions();
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("guests", guestService.getAll());
        return "guest/list";
    }

    // =====================================================================
    // ADD - completely separate pair of methods from EDIT below,
    // so a bug in one flow can never leak into the other.
    // =====================================================================

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("guest", new Guest());
        model.addAttribute("editMode", false);
        return "guest/form";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @Valid @ModelAttribute("guest") Guest guest,
                       BindingResult bindingResult, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            return "guest/form";
        }
        log.info("Adding new guest {}", guest);
        guestService.addGuest(guest);
        return "redirect:/guest/list";
    }

    // =====================================================================
    // EDIT - id always comes from the path variable, never from the form.
    // =====================================================================

    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session, @PathVariable int id, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        Guest guest = guestService.getById(id);
        if (guest == null) {
            return "redirect:/guest/list";
        }
        model.addAttribute("guest", guest);
        model.addAttribute("editMode", true);
        return "guest/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(HttpSession session, @PathVariable int id, @Valid @ModelAttribute("guest") Guest guest,
                        BindingResult bindingResult, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            return "guest/form";
        }
        log.info("Updating guest id {} with {}", id, guest);
        guestService.updateGuest(id, guest);
        return "redirect:/guest/list";
    }

    // =====================================================================
    // DELETE
    // =====================================================================

    @GetMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable int id) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        log.info("Deleting guest with id {}", id);
        guestService.deleteById(id);
        return "redirect:/guest/list";
    }
}
