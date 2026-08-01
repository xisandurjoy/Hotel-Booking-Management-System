package com.sparktech.hotel.service;

import com.sparktech.hotel.entity.User;
import com.sparktech.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Returns false when the username/email is already taken.
    public boolean register(User user) {
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())
                || userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            return false;
        }
        user.setId(null);
        userRepository.save(user);
        return true;
    }

    // Returns the user on success, null on bad credentials.
    public User authenticate(String username, String rawPassword) {
        return userRepository.findByUsernameIgnoreCase(username)
                .filter(u -> u.getPassword().equals(rawPassword))
                .orElse(null);
    }
}
