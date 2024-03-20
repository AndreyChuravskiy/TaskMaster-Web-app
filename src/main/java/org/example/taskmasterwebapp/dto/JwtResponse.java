package org.example.taskmasterwebapp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
