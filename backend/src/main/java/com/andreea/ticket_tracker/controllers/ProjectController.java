package com.andreea.ticket_tracker.controllers;

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
    public Project createProject(@RequestBody Project project){
        return projectService.createProject(project);
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/project/{id}")
    public Project getProject(@PathVariable Long id){
        return projectService.getProject(id);
    }

    @PutMapping("/project/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project){
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/project/{id}")
    public String deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return "Project deleted: " + id;
    }
}
