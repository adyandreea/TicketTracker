package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.mapper.ProjectDTOMapper;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO dto){
        Project project = ProjectDTOMapper.toEntity(dto);
        Project saved = projectRepository.save(project);

        return ProjectDTOMapper.toDTO(saved);
    }

    public List<ProjectResponseDTO> getAllProjects(){
        return projectRepository.findAll()
                .stream()
                .map(ProjectDTOMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO getProject(Long id){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return ProjectDTOMapper.toDTO(project);
    }

    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not Found"));

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        Project updated = projectRepository.save(project);
        return ProjectDTOMapper.toDTO(updated);
    }

    public void deleteProject(Long id){
        projectRepository.deleteById(id);
    }
}
