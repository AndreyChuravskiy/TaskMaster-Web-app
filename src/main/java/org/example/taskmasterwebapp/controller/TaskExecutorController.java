package org.example.taskmasterwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.domain.Task;
import org.example.taskmasterwebapp.service.AuthService;
import org.example.taskmasterwebapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
            if (task.getExecutorName().equals(authService.getAuthInfo().getUsername())) {
                return ResponseEntity.ok(task);
            } else
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Executor attempts to access an inaccessible task.");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
    }

    public String markAsComplete() {
        return "ok";
    }

    public String getAllTasks() {
        return "ok";
    }

}
