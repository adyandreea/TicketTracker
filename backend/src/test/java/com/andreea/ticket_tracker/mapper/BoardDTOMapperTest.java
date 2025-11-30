package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Project;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardDTOMapperTest {

    @Test
    void testToEntity(){

        Project project = new Project();
        project.setId(1L);
        project.setName("Project");
        project.setDescription("Desc");

        BoardRequestDTO dto = new BoardRequestDTO();
        dto.setName("Board");
        dto.setDescription("Desc");
        dto.setProjectId(project.getId());

        Board board = BoardDTOMapper.toEntity(dto, project);

        assertThat(board).isNotNull();
        assertThat(project).isNotNull();
        assertThat(board.getName()).isEqualTo(dto.getName());
        assertThat(board.getDescription()).isEqualTo(dto.getDescription());
        assertThat(project.getId()).isEqualTo(dto.getProjectId());
    }

    @Test
    void testToDTO() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Project");
        project.setDescription("Desc");

        Board board = new Board();
        board.setName("Board");
        board.setDescription("Desc");
        board.setProject(project);

        BoardResponseDTO dto = BoardDTOMapper.toDTO(board);

        assertThat(board).isNotNull();
        assertThat(project).isNotNull();
        assertThat(dto.getName()).isEqualTo(board.getName());
        assertThat(dto.getDescription()).isEqualTo(board.getDescription());
        assertThat(dto.getProjectId()).isEqualTo(project.getId());
    }
}
