package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.dto.response.UserResponseDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.entity.User;
import com.andreea.ticket_tracker.exceptions.ProjectNotFoundException;
import com.andreea.ticket_tracker.exceptions.UserNotFoundException;
import com.andreea.ticket_tracker.mapper.UserDTOMapper;
import com.andreea.ticket_tracker.mapper.ProjectDTOMapper;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import com.andreea.ticket_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO dto){
        Project project = ProjectDTOMapper.toEntity(dto);
        Project savedProject = projectRepository.save(project);
        return ProjectDTOMapper.toDTO(savedProject);
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

    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto){
        Project project = projectRepository.findById(id)
                .orElseThrow(ProjectNotFoundException::new);

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        Project savedProject = projectRepository.save(project);
        return ProjectDTOMapper.toDTO(savedProject);
    }

    public void deleteProject(Long id){
        projectRepository.findById(id)
                .orElseThrow(ProjectNotFoundException::new);

        projectRepository.deleteById(id);
    }

    public void assignUserToProject(Long projectId, Long userId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        project.addUser(user);
        projectRepository.save(project);
    }

    public List<UserResponseDTO> getProjectMembers(Long projectId){
        projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return userRepository.findAllByProjects_Id(projectId)
                .stream()
                .map(userDTOMapper::toDTO)
                .toList();
    }

    public void removeUserFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        project.removeUser(user);
        projectRepository.save(project);
    }
}
