package dev.kiri.bankAPI.application.service.user;

import dev.kiri.bankAPI.adapter.persistence.UserRepository;
import dev.kiri.bankAPI.application.exception.DuplicateUserException;
import dev.kiri.bankAPI.application.exception.UserNotFoundException;
import dev.kiri.bankAPI.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @Mock private UserRepository userRepository;
    @InjectMocks private UserServiceImpl userService;

    @Test
    void testCreateUser_Success() {
        User user =  new User("user", "user@gmail.com");
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertEquals(user, savedUser);
    }

    @Test
    void testCreateUser_DuplicateUser() {
        User user =  new User("user", "user@gmail.com");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        DuplicateUserException ex = assertThrows(DuplicateUserException.class, () -> userService.createUser(user));
        assertEquals("User already exists for username: user", ex.getMessage());
    }

    @Test
    void testGetUserById_Success() {
        Long userId = 1L;
        User user = new User("user", "user@gmail.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User fetchedUser = userService.getUserById(userId);

        assertEquals(user, fetchedUser);
    }

    @Test
    void testGetUserById_UserNotFoundException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        assertEquals("User was not found for id: 1", ex.getMessage());
    }
}