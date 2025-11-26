package com.andreea.ticket_tracker.security.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User {

        @Id
        @GeneratedValue
        private Integer id;
        private String firstname;
        private String lastname;
        private String email;
        private String username;
        private String password;

        @Enumerated(EnumType.STRING)
        private Role role;
}
