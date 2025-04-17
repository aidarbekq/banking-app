package com.example.banking.service;

import com.example.banking.entity.User;
import com.example.banking.exception.ResourceNotFoundException;
import com.example.banking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void createUser_shouldSaveUserSuccessfully() {
        String name = "Test User";
        String email = "test@example.com";

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.createUser(name, email);

        assertThat(saved.getName()).isEqualTo(name);
        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(saved.getRegistrationDate()).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_shouldReturnUserIfExists() {
        User user = new User(1L, "Aidar", "aidar@example.com", LocalDateTime.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.getUserById(1L);

        assertThat(found).isEqualTo(user);
    }

    @Test
    void getUserById_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользователь не найден");
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "Aidar", "a1@example.com", LocalDateTime.now()),
                new User(2L, "Bek", "b2@example.com", LocalDateTime.now())
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }
}
