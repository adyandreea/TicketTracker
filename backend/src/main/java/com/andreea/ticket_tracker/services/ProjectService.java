package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project){
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Project getProject(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project updateProject(Long id, Project newProject){
        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(newProject.getName());
                    project.setDescription(newProject.getDescription());
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new RuntimeException("Project not Found"));
    }

    public void deleteProject(Long id){
        projectRepository.deleteById(id);
    }
}
