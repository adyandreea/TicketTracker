package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.exceptions.ProjectNotFoundException;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void testCreateProject(){

        //given
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("Test Project");
        dto.setDescription("Description");

        //when
        projectService.createProject(dto);

        //then
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testGetAllProjects(){

        //given
        Project p1 = new Project();
        p1.setId(1L);
        p1.setName("P1");
        p1.setDescription("Description");
        Project p2 = new Project();
        p1.setId(2L);
        p2.setName("P2");
        p1.setDescription("Description");

        when(projectRepository.findAll()).thenReturn(List.of(p1, p2));

        //when
        var result = projectService.getAllProjects();

        //then
        assertEquals(2,result.size());
        verify(projectRepository,times(1)).findAll();
    }

    @Test
    void testGetProjectWhenExists(){

        //given
        Project project = new Project();
        project.setName("test");
        project.setDescription("Desc");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        //when
        var result = projectService.getProject(1L);

        //then
        assertEquals("test", result.getName());
        assertEquals("Desc", result.getDescription());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProjectWhenNotFound(){
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProject(1L));
    }

    @Test
    void testUpdateProject(){

        //given
        Project p1 = new Project();
        p1.setName("Old");
        p1.setDescription("Old Description");

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("New project");
        dto.setDescription("New Description");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(p1));

        //when
        projectService.updateProject(1L, dto);

        //then
        verify(projectRepository).save(p1);
        assertEquals("New project", p1.getName());
        assertEquals("New Description", p1.getDescription());
    }

    @Test
    void testDeleteProject(){

        //given
        Project p1 = new Project();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(p1));

        //when
        projectService.deleteProject(1L);

        //then
        verify(projectRepository).deleteById(1L);
    }
}
