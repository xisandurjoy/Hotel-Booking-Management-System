package com.sparktech.hotel.controller;

import com.sparktech.hotel.entity.User;
import com.sparktech.hotel.service.UserService;
import com.sparktech.hotel.util.AuthCheck;
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
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // =====================================================================
    // REGISTER
    // =====================================================================

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        boolean success = userService.register(user);
        if (!success) {
            model.addAttribute("error", "Username or email is already taken");
            return "register";
        }

        log.info("New user registered: {}", user.getUsername());
        return "redirect:/login?registered";
    }

    // =====================================================================
    // LOGIN / LOGOUT
    // =====================================================================

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                         HttpSession session, Model model) {
        User user = userService.authenticate(username, password);
        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        session.setAttribute("loggedInUser", user.getUsername());
        log.info("User {} logged in", user.getUsername());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // =====================================================================
    // DASHBOARD
    // =====================================================================

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (!AuthCheck.isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "home";
    }
}
