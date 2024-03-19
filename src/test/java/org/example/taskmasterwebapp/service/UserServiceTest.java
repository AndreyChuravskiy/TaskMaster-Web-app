package org.example.taskmasterwebapp.service;

import org.example.taskmasterwebapp.domain.User;
import org.example.taskmasterwebapp.domain.UserRole;
import org.example.taskmasterwebapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(2, "andrey", "12345", Set.of(new UserRole("EXECUTOR") ,new UserRole("ISSUER")));
    }

    @Test
    void findUserByUsername() {
        String username = "andrey";

        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findUserByUsername(any(String.class))).thenReturn(optionalUser);

        Optional<User> returnedOptionalUser = userService.findUserByUsername(username);

        Assertions.assertEquals(returnedOptionalUser, optionalUser);

        verify(userRepository, times(1)).findUserByUsername(any(String.class));
    }

    @Test
    void createUser() {
        String username = "andrey";
        String password = "12345";
        String role1 = "ISSUER";
        String role2 = "EXECUTOR";
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(username, password, role1, role2);

        verify(userRepository, times(1)).save(any(User.class));
    }
}
