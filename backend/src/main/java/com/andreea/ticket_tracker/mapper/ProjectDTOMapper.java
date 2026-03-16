package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.entity.Project;

/**
 * Mapper utility to convert between Project entities and DTOs.
 */
public class ProjectDTOMapper {

    /**
     * Converts a ProjectRequestDTO to a Project entity.
     * @param dto the request data transfer object
     * @return a new Project entity
     */
    public static Project toEntity(ProjectRequestDTO dto){
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        return project;
    }

    /**
     * Converts a Project entity to a ProjectResponseDTO.
     * @param project the entity to convert
     * @return the populated response DTO
     */
    public static ProjectResponseDTO toDTO(Project project){
        ProjectResponseDTO dto = new ProjectResponseDTO();

        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());

        dto.setBoardCount(project.getBoards() != null ? project.getBoards().size() : 0);

        return dto;
    }
}
