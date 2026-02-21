package com.andreea.ticket_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

        @Id
        @GeneratedValue
        private Long id;

        @NotBlank(message = "firstname_required")
        private String firstname;

        @NotBlank(message = "lastname_required")
        private String lastname;

        @NotBlank(message = "username_required")
        @Size(min = 3, max = 20, message = "username_length_invalid")
        private String username;

        @NotBlank(message = "email_required")
        @Email(message = "email_invalid")
        private String email;

        @NotBlank(message = "password_required")
        @Size(min = 6, message = "password_length_invalid")
        private String password;

        @NotNull(message = "role_required")
        @Enumerated(EnumType.STRING)
        private Role role;

        @Builder.Default
        @ManyToMany(mappedBy="users", cascade=CascadeType.ALL)
        private Set<Project> projects = new HashSet<>();
}
