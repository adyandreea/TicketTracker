package com.andreea.ticket_tracker.security.auth;

import com.andreea.ticket_tracker.security.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private Role role;
}