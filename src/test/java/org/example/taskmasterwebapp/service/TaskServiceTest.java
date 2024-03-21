package org.example.taskmasterwebapp.service;

import org.example.taskmasterwebapp.domain.Task;
import org.example.taskmasterwebapp.dto.CreateTaskRequest;
import org.example.taskmasterwebapp.repository.TaskRepository;
import org.example.taskmasterwebapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask() {
        CreateTaskRequest createTaskRequest = new CreateTaskRequest("task1", "task1task1task1", "misha");
        Task task = new Task("task1", "task1task1task1", "misha");
        task.setId(11L);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task returnedTask = taskService.createTask(createTaskRequest);

        Assertions.assertEquals(returnedTask, task);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void findTaskById() {
        Task task = new Task("task1", "task1task1task1", "misha");
        task.setId(11L);
        Optional<Task> optionalTask = Optional.of(task);

        when(taskRepository.findById(any(Long.class))).thenReturn(optionalTask);

        Optional<Task> returnedTask = taskService.findTaskById(11L);

        Assertions.assertEquals(returnedTask, optionalTask);

        verify(taskRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void findAllTasks() {
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

        when(taskRepository.findAll()).thenReturn(taskList);

        List<Task> returnedList = taskService.findAllTasks();

        Assertions.assertEquals(returnedList, taskList);

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void findAllTasksByExecutorName() {
        String executorName = "misha";
        Task task1 = new Task("task1", "task1task1task1", "misha");
        Task task2 = new Task("task2", "task2task2task2", "misha");
        Task task3 = new Task("task3", "task3task3task3", "misha");

        task1.setId(1L);
        task2.setId(2L);
        task3.setId(3L);
        task1.setCompleted(true);
        List<Task> taskList = List.of(task1, task2, task3);

        when(taskRepository.findAllByExecutorName(any(String.class))).thenReturn(taskList);

        List<Task> returnedTaskList = taskService.findAllTasksByExecutorName("misha");

        Assertions.assertEquals(returnedTaskList, taskList);

        verify(taskRepository, times(1)).findAllByExecutorName(any(String.class));
    }

    @Test
    void deleteTask() {
        doNothing().when(taskRepository).deleteById(any(Long.class));

        taskService.deleteTask(11L);

        verify(taskRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void markTaskAsComplete() {
        Task task = new Task("task1", "task1task1task1", "misha");
        task.setId(11L);
        task.setCompleted(false);

        Task task2 = new Task("task1", "task1task1task1", "misha");
        task.setId(11L);
        task.setCompleted(true);

        when(taskRepository.save(any(Task.class))).thenReturn(task2);

        Task returnedTask = taskService.markTaskAsComplete(task);

        Assertions.assertEquals(task2, returnedTask);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markTaskAsIncomplete() {
        Task task = new Task("task1", "task1task1task1", "misha");
        task.setId(11L);
        task.setCompleted(true);

        Task task2 = new Task("task1", "task1task1task1", "misha");
        task.setId(11L);
        task.setCompleted(false);

        when(taskRepository.save(any(Task.class))).thenReturn(task2);

        Task returnedTask = taskService.markTaskAsComplete(task);

        Assertions.assertEquals(task2, returnedTask);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

}
