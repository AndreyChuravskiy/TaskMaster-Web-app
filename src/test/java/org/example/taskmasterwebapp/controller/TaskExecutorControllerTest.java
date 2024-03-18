package org.example.taskmasterwebapp.controller;

import org.example.taskmasterwebapp.domain.Task;
import org.example.taskmasterwebapp.domain.UserRole;
import org.example.taskmasterwebapp.dto.JwtAuthentication;
import org.example.taskmasterwebapp.dto.JwtRequest;
import org.example.taskmasterwebapp.filter.JwtFilter;
import org.example.taskmasterwebapp.service.AuthService;
import org.example.taskmasterwebapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskExecutorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskExecutorControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getTaskById() throws Exception{
        Task task = new Task("test", "Testing tests", "andrey");
        task.setId(4L);
        Optional<Task> optionalTask = Optional.of(task);

        JwtAuthentication authInfo = new JwtAuthentication();
        authInfo.setUsername("andrey");
        authInfo.setAuthenticated(true);
        authInfo.setRoles(Set.of(new UserRole("EXECUTOR")));

        when(taskService.findTaskById(any(Long.class))).thenReturn(optionalTask);
        when(authService.getAuthInfo()).thenReturn(authInfo);

        mockMvc.perform(get("/api/executor/tasks/{id}", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.description").value("Testing tests"))
                .andExpect(jsonPath("$.executorName").value("andrey"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(authService, times(1)).getAuthInfo();
        verify(taskService, times(1)).findTaskById(any(Long.class));
    }

    @Test
    void markTaskAsComplete() throws Exception{
        Task task = new Task("test", "Testing tests", "andrey");
        task.setId(5L);
        Optional<Task> optionalTask = Optional.of(task);

        JwtAuthentication authInfo = new JwtAuthentication();
        authInfo.setUsername("andrey");
        authInfo.setAuthenticated(true);
        authInfo.setRoles(Set.of(new UserRole("EXECUTOR")));

        when(taskService.findTaskById(any(Long.class))).thenReturn(optionalTask);
        task.setCompleted(true);
        when(taskService.markTaskAsComplete(any(Task.class))).thenReturn(task);
        when(authService.getAuthInfo()).thenReturn(authInfo);


        mockMvc.perform(post("/api/executor/tasks/{id}/mark-as-complete", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.description").value("Testing tests"))
                .andExpect(jsonPath("$.executorName").value("andrey"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(authService, times(1)).getAuthInfo();
        verify(taskService, times(1)).findTaskById(any(Long.class));
        verify(taskService, times(1)).markTaskAsComplete(any(Task.class));
    }

    @Test
    void markTaskAsIncomplete() throws Exception{
        Task task = new Task("test", "Testing tests", "andrey");
        task.setId(5L);
        task.setCompleted(true);
        Optional<Task> optionalTask = Optional.of(task);

        JwtAuthentication authInfo = new JwtAuthentication();
        authInfo.setUsername("andrey");
        authInfo.setAuthenticated(true);
        authInfo.setRoles(Set.of(new UserRole("EXECUTOR")));

        when(taskService.findTaskById(any(Long.class))).thenReturn(optionalTask);
        task.setCompleted(false);
        when(taskService.markTaskAsIncomplete(any(Task.class))).thenReturn(task);
        when(authService.getAuthInfo()).thenReturn(authInfo);


        mockMvc.perform(post("/api/executor/tasks/{id}/mark-as-incomplete", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.description").value("Testing tests"))
                .andExpect(jsonPath("$.executorName").value("andrey"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(authService, times(1)).getAuthInfo();
        verify(taskService, times(1)).findTaskById(any(Long.class));
        verify(taskService, times(1)).markTaskAsIncomplete(any(Task.class));
    }

    @Test
    void getAllTasks() throws Exception {
        Task task1 = new Task("task1", "task1task1task1", "andrey");
        Task task3 = new Task("task3", "task3task3task3", "andrey");
        Task task5 = new Task("task5", "task5task5task5", "andrey");
        task1.setId(1L);
        task3.setId(3L);
        task5.setId(5L);
        task1.setCompleted(true);
        List<Task> taskList = List.of(task1, task3, task5);

        JwtAuthentication authInfo = new JwtAuthentication();
        authInfo.setUsername("andrey");
        authInfo.setAuthenticated(true);
        authInfo.setRoles(Set.of(new UserRole("EXECUTOR")));

        when(authService.getAuthInfo()).thenReturn(authInfo);
        when(taskService.findAllTasksByExecutorName(any(String.class))).thenReturn(taskList);

        mockMvc.perform(get("/api/executor/tasks/get-all"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("task1"))
                .andExpect(jsonPath("$[0].description").value("task1task1task1"))
                .andExpect(jsonPath("$[0].executorName").value("andrey"))
                .andExpect(jsonPath("$[0].completed").value(true))

                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].title").value("task3"))
                .andExpect(jsonPath("$[1].description").value("task3task3task3"))
                .andExpect(jsonPath("$[1].executorName").value("andrey"))
                .andExpect(jsonPath("$[1].completed").value(false))

                .andExpect(jsonPath("$[2].id").value(5))
                .andExpect(jsonPath("$[2].title").value("task5"))
                .andExpect(jsonPath("$[2].description").value("task5task5task5"))
                .andExpect(jsonPath("$[2].executorName").value("andrey"))
                .andExpect(jsonPath("$[2].completed").value(false));

        verify(authService, times(1)).getAuthInfo();
        verify(taskService, times(1)).findAllTasksByExecutorName(any(String.class));
    }
}
