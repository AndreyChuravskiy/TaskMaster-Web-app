package org.example.taskmasterwebapp.service;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.domain.UserRefreshTokenMapping;
import org.example.taskmasterwebapp.exception.UserNotFoundException;
import org.example.taskmasterwebapp.repository.RefreshStorageRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshStorageUtils {

    private final RefreshStorageRepository refreshStorageRepository;

    public void put(String username, String token) {
        Optional<UserRefreshTokenMapping> optionalMapping = refreshStorageRepository.findByUsername(username);
        if (optionalMapping.isPresent()) {
            UserRefreshTokenMapping mapping = optionalMapping.get();
            mapping.setRefreshToken(token);
            refreshStorageRepository.save(mapping);
        } else {
            UserRefreshTokenMapping mapping = new UserRefreshTokenMapping(username, token);
            refreshStorageRepository.save(mapping);
        }
    }

    public String get(String username) {
        Optional<UserRefreshTokenMapping> optionalMapping = refreshStorageRepository.findByUsername(username);
        if (optionalMapping.isPresent()) {
            return optionalMapping.get().getRefreshToken();
        } else
            throw new UserNotFoundException("User not found");
    }
}
