package org.example.taskmasterwebapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
@Getter
@Setter
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 60)
    private String name;

    public UserRole(String name) {
        this.name = name;
    }

    public UserRole() {

    }

    @Override
    public String getAuthority() {
        return name;
    }
}
