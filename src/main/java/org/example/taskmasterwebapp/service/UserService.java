package org.example.taskmasterwebapp.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.domain.User;
import org.example.taskmasterwebapp.domain.UserRole;
import org.example.taskmasterwebapp.exception.UserNotFoundException;
import org.example.taskmasterwebapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    public void createUser(@NonNull String username, @NonNull String password, @NonNull String... roleNames) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Set<UserRole> roles = new HashSet<>();
        for (String role : roleNames) {
            roles.add(new UserRole(role));
        }

        user.setRoles(roles);
        userRepository.save(user);
    }
}
