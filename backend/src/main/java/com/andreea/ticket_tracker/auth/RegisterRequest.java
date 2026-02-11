package com.andreea.ticket_tracker.auth;

import com.andreea.ticket_tracker.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

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
    @Size(min = 6, message = "password_length_required")
    private String password;

    @NotNull(message = "role_required")
    private Role role;
}
