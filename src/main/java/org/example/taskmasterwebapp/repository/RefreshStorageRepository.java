package org.example.taskmasterwebapp.repository;

import org.example.taskmasterwebapp.domain.UserRefreshTokenMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshStorageRepository extends JpaRepository<UserRefreshTokenMapping, Long> {

    public Optional<UserRefreshTokenMapping> findByUsername(String username);
}
