package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.ProjectResponseDTO;
import com.andreea.ticket_tracker.entity.Project;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDTOMapperTest {

    @Test
    void testToEntity() {

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("Project");
        dto.setDescription("Desc");

        Project project = ProjectDTOMapper.toEntity(dto);

        assertThat(project).isNotNull();
        assertThat(project.getName()).isEqualTo(dto.getName());
        assertThat(project.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    void testToDTO() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Project");
        project.setDescription("Desc");

        ProjectResponseDTO dto = ProjectDTOMapper.toDTO(project);

        assertThat(project).isNotNull();
        assertThat(dto.getId()).isEqualTo(project.getId());
        assertThat(dto.getName()).isEqualTo(project.getName());
        assertThat(dto.getDescription()).isEqualTo(project.getDescription());
    }
}
