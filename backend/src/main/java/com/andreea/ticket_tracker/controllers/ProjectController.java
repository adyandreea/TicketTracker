package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.services.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping("/project")
    public ProjectResponseDTO createProject(@RequestBody ProjectRequestDTO dto){
        return projectService.createProject(dto);
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
    public ProjectResponseDTO updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO dto){
        return projectService.updateProject(id, dto);
    }

    @DeleteMapping("/project/{id}")
    public String deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return "Project deleted: " + id;
    }
}
