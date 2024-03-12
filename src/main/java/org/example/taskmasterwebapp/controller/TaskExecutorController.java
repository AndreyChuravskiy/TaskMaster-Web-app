package org.example.taskmasterwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.domain.Task;
import org.example.taskmasterwebapp.service.AuthService;
import org.example.taskmasterwebapp.service.TaskService;
import org.hibernate.engine.spi.CollectionEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/executor")
@PreAuthorize("hasAuthority('EXECUTOR')")
@RequiredArgsConstructor
public class TaskExecutorController {

    private final TaskService taskService;

    private final AuthService authService;
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> optionalTask = taskService.findTaskById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            if (task.getExecutorName().equals(authService.getAuthInfo().getPrincipal())) {
                return ResponseEntity.ok(task);
            } else
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Executor attempts to access an inaccessible task.");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }

    @PostMapping("/tasks/{id}/mark-as-complete")
    public ResponseEntity<Task> markTaskAsComplete(@PathVariable Long id) {
        Optional<Task> optionalTask = taskService.findTaskById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            String username = (String) authService.getAuthInfo().getPrincipal();
            if (username.equals(task.getExecutorName())) {
                return ResponseEntity.ok(taskService.markTaskAsComplete(task));
            } else
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Executor attempts to access an inaccessible task.");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }

    @PostMapping("/tasks/{id}/mark-as-incomplete")
    public ResponseEntity<Task> markTaskAsIncomplete(@PathVariable Long id) {
        Optional<Task> optionalTask = taskService.findTaskById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            String username = (String) authService.getAuthInfo().getPrincipal();
            if (username.equals(task.getExecutorName())) {
                return ResponseEntity.ok(taskService.markTaskAsIncomplete(task));
            } else
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Executor attempts to access an inaccessible task.");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }

    @GetMapping("/tasks/get-all")
    public ResponseEntity<List<Task>> getAllTasks() {
        String username = (String) authService.getAuthInfo().getPrincipal();
        return ResponseEntity.ok(taskService.findAllTasksByExecutorName(username));
    }

}
