package org.example.taskmasterwebapp.service;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.domain.Task;
import org.example.taskmasterwebapp.dto.CreateTaskRequest;
import org.example.taskmasterwebapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(CreateTaskRequest request) {
        Task task = new Task(request.getTitle(), request.getDescription(), request.getExecutor());
        return taskRepository.save(task);
    }

    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }
}
