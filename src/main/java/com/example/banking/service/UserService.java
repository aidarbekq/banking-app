package com.example.banking.service;

import com.example.banking.entity.User;
import com.example.banking.exception.ResourceNotFoundException;
import com.example.banking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email) {
        // Можно проверить существование email
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email уже существует: " + email);
        }
        User user = new User(name, email, LocalDateTime.now());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден, id = " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
