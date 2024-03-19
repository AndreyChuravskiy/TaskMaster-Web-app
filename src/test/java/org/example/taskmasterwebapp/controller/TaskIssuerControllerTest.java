package org.example.taskmasterwebapp.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.taskmasterwebapp.domain.Task;
import org.example.taskmasterwebapp.domain.User;
import org.example.taskmasterwebapp.domain.UserRole;
import org.example.taskmasterwebapp.dto.CreateTaskRequest;
import org.example.taskmasterwebapp.dto.JwtAuthentication;
import org.example.taskmasterwebapp.filter.JwtFilter;
import org.example.taskmasterwebapp.service.TaskService;
import org.example.taskmasterwebapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskIssuerController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskIssuerControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private JwtAuthentication authInfo;

    @BeforeEach
    void setUp() {
        authInfo = new JwtAuthentication();
        authInfo.setUsername("misha");
        authInfo.setAuthenticated(true);
        authInfo.setRoles(Set.of(new UserRole("ISSUER")));
    }

    @Test
    void createTask() throws Exception {
        CreateTaskRequest createTaskRequest = new CreateTaskRequest("task1"
                ,"testingtestingtesting"
                ,"andrey");
        String createTaskRequestJson = objectMapper.writeValueAsString(createTaskRequest);
        User user = new User(2, "andrey", "12345", Set.of(new UserRole("EXECUTOR")));
        Optional<User> optionalUser = Optional.of(user);

        Task task = new Task("task1", "testingtestingtesting", "andrey");
        task.setId(2L);

        when(userService.findUserByUsername(any(String.class))).thenReturn(optionalUser);
        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(task);

        mockMvc.perform(post("/api/issuer/create-task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTaskRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("task1"))
                .andExpect(jsonPath("$.description").value("testingtestingtesting"))
                .andExpect(jsonPath("$.executorName").value("andrey"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(userService, times(1)).findUserByUsername(any(String.class));
        verify(taskService, times(1)).createTask(any(CreateTaskRequest.class));
    }

    @Test
    void getTaskById() throws Exception{
        Long id = 2L;

        Task task = new Task("task1", "testingtestingtesting", "andrey");
        task.setId(2L);
        task.setCompleted(true);
        Optional<Task> optionalTask = Optional.of(task);

        when(taskService.findTaskById(any(Long.class))).thenReturn(optionalTask);

        mockMvc.perform(get("/api/issuer/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                        .andExpect(jsonPath("$.title").value("task1"))
                        .andExpect(jsonPath("$.description").value("testingtestingtesting"))
                        .andExpect(jsonPath("$.executorName").value("andrey"))
                        .andExpect(jsonPath("$.completed").value(true));

        verify(taskService, times(1)).findTaskById(any(Long.class));
    }

    @Test
    void getAllTasks() throws Exception {
        Task task1 = new Task("task1", "task1task1task1", "andrey");
        Task task2 = new Task("task2", "task2task2task2", "petia");
        Task task3 = new Task("task3", "task3task3task3", "andrey");
        Task task4 = new Task("task4", "task4task4task4", "vlad");
        Task task5 = new Task("task5", "task5task5task5", "andrey");
        task1.setId(1L);
        task2.setId(2L);
        task3.setId(3L);
        task4.setId(4L);
        task5.setId(5L);
        task1.setCompleted(true);
        task4.setCompleted(true);
        List<Task> taskList = List.of(task1, task2, task3, task4, task5);

        when(taskService.findAllTasks()).thenReturn(taskList);

        mockMvc.perform(get("/api/issuer/tasks/get-all"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$", hasSize(5)))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("task1"))
                .andExpect(jsonPath("$[0].description").value("task1task1task1"))
                .andExpect(jsonPath("$[0].executorName").value("andrey"))
                .andExpect(jsonPath("$[0].completed").value(true))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("task2"))
                .andExpect(jsonPath("$[1].description").value("task2task2task2"))
                .andExpect(jsonPath("$[1].executorName").value("petia"))
                .andExpect(jsonPath("$[1].completed").value(false))

                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].title").value("task3"))
                .andExpect(jsonPath("$[2].description").value("task3task3task3"))
                .andExpect(jsonPath("$[2].executorName").value("andrey"))
                .andExpect(jsonPath("$[2].completed").value(false))

                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].title").value("task4"))
                .andExpect(jsonPath("$[3].description").value("task4task4task4"))
                .andExpect(jsonPath("$[3].executorName").value("vlad"))
                .andExpect(jsonPath("$[3].completed").value(true))

                .andExpect(jsonPath("$[4].id").value(5))
                .andExpect(jsonPath("$[4].title").value("task5"))
                .andExpect(jsonPath("$[4].description").value("task5task5task5"))
                .andExpect(jsonPath("$[4].executorName").value("andrey"))
                .andExpect(jsonPath("$[4].completed").value(false));

        verify(taskService, times(1)).findAllTasks();
    }

    @Test
    void deleteTask() throws Exception{
        Long id = 4L;

        Task task = new Task("task1", "testingtestingtesting", "andrey");
        task.setId(4L);
        Optional<Task> optionalTask = Optional.of(task);

        when(taskService.findTaskById(any(Long.class))).thenReturn(optionalTask);
        doNothing().when(taskService).deleteTask(any(Long.class));

        mockMvc.perform(post("/api/issuer/tasks/{id}/delete", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Task was deleted"));

        verify(taskService, times(1)).findTaskById(any(Long.class));
        verify(taskService, times(1)).deleteTask(any(Long.class));
    }
}
