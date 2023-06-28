package com.example.springpersonwebflux.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;

@Table("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

     @ToString.Include(name = "password")
    private String maskPassword() {
        return "*******";
    }

}