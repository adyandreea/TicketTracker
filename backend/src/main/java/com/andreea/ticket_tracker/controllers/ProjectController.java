package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.dto.response.SuccessDTO;
import com.andreea.ticket_tracker.handler.ResponseHandler;
import com.andreea.ticket_tracker.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping("/project")
    public ResponseEntity<SuccessDTO> createProject(@RequestBody ProjectRequestDTO dto){
        projectService.createProject(dto);
        return ResponseHandler.created("Project created successfully");
    }

    @GetMapping("/projects")
    public List<ProjectResponseDTO> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/project/{id}")
    public ProjectResponseDTO getProject(@PathVariable Long id){
        return projectService.getProject(id);
    }

    @PutMapping("/project/{id}")
    public ResponseEntity<SuccessDTO> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO dto){
        projectService.updateProject(id, dto);
        return ResponseHandler.updated("Project updated successfully");
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<SuccessDTO> deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return ResponseHandler.deleted("Project deleted successfully");
    }
}
