package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ErrorDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.dto.response.SuccessDTO;
import com.andreea.ticket_tracker.dto.response.UserResponseDTO;
import com.andreea.ticket_tracker.handler.ResponseHandler;
import com.andreea.ticket_tracker.services.ProjectService;
import com.andreea.ticket_tracker.swagger.SwaggerHttpStatus;
import com.andreea.ticket_tracker.swagger.SwaggerMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing projects.
 */
@Tag(name = "Project API", description = "API for project management")
@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "${frontend.url}")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService){
        this.projectService = projectService;
    }

    /**
     * Endpoint to create a new project.
     * @param dto project data
     * @return response entity with created project
     */
    @Operation(summary = "Creates a new project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.PROJECT_SUCCESSFULLY_CREATED,
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
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO dto){
        ProjectResponseDTO createdProject = projectService.createProject(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProject);
    }

    /**
     * Endpoint to retrieve all projects for the current user.
     * @return list of project DTOs
     */
    @Operation(summary = "Returns projects based on user role (all for ADMIN/MANAGER, assigned for USER).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.RETURN_PROJECTS,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectResponseDTO[].class))})
    })
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    /**
     * Endpoint to get a specific project by ID.
     * @param id project ID
     * @return project details
     */
    @Operation(summary = "Returns a project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.RETURN_PROJECT,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectResponseDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @GetMapping("/{id}")
    public ProjectResponseDTO getProject(@PathVariable Long id){
        return projectService.getProject(id);
    }

    /**
     * Endpoint to update a project.
     * @param id project ID
     * @param dto updated data
     * @return updated project
     */
    @Operation(summary = "Updates the project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.PROJECT_SUCCESSFULLY_UPDATED,
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
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id, @Valid   @RequestBody ProjectRequestDTO dto){
        ProjectResponseDTO updatedProject = projectService.updateProject(id, dto);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * Endpoint to delete a project.
     * @param id project ID
     * @return success message
     */
    @Operation(summary = "Deletes the project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.PROJECT_SUCCESSFULLY_DELETED,
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
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessDTO> deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return ResponseHandler.deleted("Project deleted successfully");
    }

    /**
     * Endpoint to assign a user to a project.
     * @param projectId project ID
     * @param userId user ID
     * @return success message
     */
    @Operation(summary = "Assigns a user to a project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.ASSIGN_USER_TO_PROJECT,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuccessDTO[].class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<SuccessDTO> assignUserToProject(@PathVariable Long projectId, @PathVariable Long userId){
        projectService.assignUserToProject(projectId, userId);
        return ResponseHandler.success("User assigned to project successfully");
    }

    /**
     * Endpoint to list all members of a project.
     * @param projectId project ID
     * @return list of users
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.RETURN_USERS_BY_PROJECT,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO[].class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @Operation(summary = "Returns all members of a specific project.")
    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<UserResponseDTO>> getProjectMembers(@PathVariable Long projectId) {
        List<UserResponseDTO> members = projectService.getProjectMembers(projectId);
        return ResponseEntity.ok(members);
    }

    /**
     * Endpoint to remove a user from a project.
     * @param projectId project ID
     * @param userId user ID
     * @return success message
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.REMOVE_USER_FROM_PROJECT,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO[].class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @Operation(summary = "Removes a user from a project.")
    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<SuccessDTO> removeUserFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.removeUserFromProject(projectId, userId);
        return ResponseHandler.success("User removed from project successfully");
    }
}
