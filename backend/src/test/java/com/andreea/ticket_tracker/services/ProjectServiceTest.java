package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.UserResponseDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.entity.Role;
import com.andreea.ticket_tracker.entity.User;
import com.andreea.ticket_tracker.exceptions.ProjectNotFoundException;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import com.andreea.ticket_tracker.repository.UserRepository;
import com.andreea.ticket_tracker.mapper.UserDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDTOMapper userDTOMapper;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void testCreateProject(){

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("Test Project");
        dto.setDescription("Description");

        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArguments()[0]);

        var result =  projectService.createProject(dto);

        assertEquals("Test Project", result.getName());
        assertEquals("Description", result.getDescription());
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

        Project p1 = new Project();
        p1.setName("Old");
        p1.setDescription("Old Description");

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("New project");
        dto.setDescription("New Description");

        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArguments()[0]);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(p1));

        projectService.updateProject(1L, dto);

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

    @Test
    void testAssignUserToProject(){
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArguments()[0]);
        projectService.assignUserToProject(1L, 1L);

        assertEquals(1, project.getUsers().size());
        verify(projectRepository, times(1)).save(project);
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProjectMembers(){
        Project project = new Project();
        User u1 = new User();
        u1.setId(1L);
        u1.setFirstname("User");
        u1.setLastname("User");
        u1.setUsername("user1");
        u1.setEmail("user@gmail.com");
        u1.setRole(Role.USER);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findAllByProjects_Id(1L)).thenReturn(Set.of(u1));
        when(userDTOMapper.toDTO(u1)).thenReturn(new UserResponseDTO(
                1L,
                "User",
                "User",
                "user1",
                "user@gmail.com",
                Role.USER
        ));

        List<UserResponseDTO> result = projectService.getProjectMembers(1L);

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAllByProjects_Id(1L);
    }

    @Test
    void testRemoveUserFromProject(){
        Project project = new Project();
        project.setId(1L);
        User user = new User();
        user.setId(1L);

        project.addUser(user);
        assertEquals(1, project.getUsers().size());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        projectService.removeUserFromProject(1L, 1L);

        assertEquals(0, project.getUsers().size());
        verify(projectRepository, times(1)).save(project);
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }
}
