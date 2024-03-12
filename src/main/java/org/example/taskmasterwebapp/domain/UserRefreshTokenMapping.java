package org.example.taskmasterwebapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refresh_storage")
@Getter
@Setter
@NoArgsConstructor
public class UserRefreshTokenMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String refreshToken;

    public UserRefreshTokenMapping(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }
}
