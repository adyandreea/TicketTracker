package com.andreea.ticket_tracker.security.auth;

import com.andreea.ticket_tracker.dto.response.ErrorDTO;
import com.andreea.ticket_tracker.dto.response.SuccessDTO;
import com.andreea.ticket_tracker.swagger.SwaggerHttpStatus;
import com.andreea.ticket_tracker.swagger.SwaggerMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}")
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(summary = "Creates a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.USER_SUCCESSFULLY_CREATED,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuccessDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(summary = "Authenticates a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.USER_SUCCESSFULLY_AUTHENTICATED,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuccessDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Operation(summary = "Returns all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = "Users successfully retrieved",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @Operation(summary = "Deletes a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = "User not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = "User successfully updated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = "Invalid user data",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody UserRequestDTO request
    ) {
        return ResponseEntity.ok(service.updateUser(id, request));
    }
}
