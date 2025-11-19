package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.exceptions.ProjectNotFoundException;
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

    public void createProject(ProjectRequestDTO dto){
        Project project = ProjectDTOMapper.toEntity(dto);
        projectRepository.save(project);
    }

    public List<ProjectResponseDTO> getAllProjects(){
        return projectRepository.findAll()
                .stream()
                .map(ProjectDTOMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO getProject(Long id){
        Project project = projectRepository.findById(id)
                .orElseThrow(ProjectNotFoundException::new);

        return ProjectDTOMapper.toDTO(project);
    }

    public void updateProject(Long id, ProjectRequestDTO dto){
        Project project = projectRepository.findById(id)
                .orElseThrow(ProjectNotFoundException::new);

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        projectRepository.save(project);
    }

    public void deleteProject(Long id){
        projectRepository.findById(id)
                .orElseThrow(ProjectNotFoundException::new);

        projectRepository.deleteById(id);
    }
}
