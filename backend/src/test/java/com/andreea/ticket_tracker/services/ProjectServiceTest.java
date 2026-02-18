package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.dto.response.UserResponseDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.entity.Role;
import com.andreea.ticket_tracker.entity.User;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import com.andreea.ticket_tracker.repository.UserRepository;
import com.andreea.ticket_tracker.mapper.UserDTOMapper;
import com.andreea.ticket_tracker.security.config.ProjectSecurityEvaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDTOMapper userDTOMapper;

    @Mock
    private ProjectSecurityEvaluator projectSecurity;

    @InjectMocks
    private ProjectService projectService;

    private void mockSecurityContext(String username) {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getName()).thenReturn(username);
    }

    @Test
    void testCreateProject(){
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("Test Project");
        dto.setDescription("Description");

        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArguments()[0]);

        var result = projectService.createProject(dto);

        assertEquals("Test Project", result.getName());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void testGetAllProjects(){
        mockSecurityContext("adminUser");
        Project p1 = new Project();

        when(projectSecurity.isUserAdmin()).thenReturn(true);
        when(projectRepository.findAll()).thenReturn(List.of(p1));

        var result = projectService.getAllProjects();

        assertEquals(1, result.size());
        verify(projectRepository).findAll();
    }

    @Test
    void testGetProjectWhenExists(){
        Project project = new Project();
        project.setName("test");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        var result = projectService.getProject(1L);

        assertEquals("test", result.getName());
        verify(projectSecurity).validateUserAccess(project);
    }

    @Test
    void testUpdateProject(){
        Project p1 = new Project();
        p1.setId(1L);
        p1.setName("Old");

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("New project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArguments()[0]);

        projectService.updateProject(1L, dto);

        verify(projectSecurity).validateUserAccess(p1);
        verify(projectRepository).save(p1);
        assertEquals("New project", p1.getName());
    }

    @Test
    void testDeleteProject(){
        Project p1 = new Project();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(p1));

        projectService.deleteProject(1L);

        verify(projectSecurity).validateUserAccess(p1);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void testAssignUserToProject(){
        Project project = new Project();
        User user = new User();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        projectService.assignUserToProject(1L, 2L);

        verify(projectSecurity).validateUserAccess(project);
        verify(projectRepository).save(project);
    }

    @Test
    void testGetProjectMembers(){
        Project project = new Project();
        User u1 = new User();
        u1.setUsername("user1");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findAllByProjects_Id(1L)).thenReturn(Set.of(u1));
        when(userDTOMapper.toDTO(u1)).thenReturn(new UserResponseDTO(1L, "U", "U", "user1", "e", Role.USER));

        List<UserResponseDTO> result = projectService.getProjectMembers(1L);

        verify(projectSecurity).validateUserAccess(project);
        assertEquals(1, result.size());
    }

    @Test
    void testRemoveUserFromProject(){
        Project project = new Project();
        User user = new User();
        user.setId(2L);
        project.addUser(user);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        projectService.removeUserFromProject(1L, 2L);

        verify(projectSecurity).validateUserAccess(project);
        verify(projectRepository).save(project);
        assertEquals(0, project.getUsers().size());
    }
}

