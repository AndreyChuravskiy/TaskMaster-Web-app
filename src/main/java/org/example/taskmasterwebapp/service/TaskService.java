package org.example.taskmasterwebapp.service;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.domain.Task;
import org.example.taskmasterwebapp.domain.User;
import org.example.taskmasterwebapp.dto.CreateTaskRequest;
import org.example.taskmasterwebapp.repository.TaskRepository;
import org.example.taskmasterwebapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public Task createTask(CreateTaskRequest request) {
        Task task = new Task(request.getTitle(), request.getDescription(), request.getExecutorName());
        return taskRepository.save(task);
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findAllTasks() { return taskRepository.findAll(); }

    public List<Task> findAllTasksByExecutorName(String executorName) {
        return taskRepository.findAllByExecutorName(executorName);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void completeTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
             task.setCompleted(true);
             taskRepository.save(task);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
    }
}
