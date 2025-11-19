package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.entity.Project;

public class ProjectDTOMapper {

    public static Project toEntity(ProjectRequestDTO dto){
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        return project;
    }

    public static ProjectResponseDTO toDTO(Project project){
        ProjectResponseDTO dto = new ProjectResponseDTO();

        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());

        dto.setBoardCount(project.getBoards() != null ? project.getBoards().size() : 0);

        return dto;
    }
}
