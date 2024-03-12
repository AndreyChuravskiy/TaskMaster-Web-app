package org.example.taskmasterwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.domain.Task;
import org.example.taskmasterwebapp.domain.User;
import org.example.taskmasterwebapp.dto.CreateTaskRequest;
import org.example.taskmasterwebapp.service.TaskService;
import org.example.taskmasterwebapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/issuer")
@PreAuthorize("hasAuthority('ISSUER')")
@RequiredArgsConstructor
public class TaskIssuerController {

    private final TaskService taskService;

    private final UserService userService;

    @PostMapping("/create-task")
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        Optional<User> optionalUser = userService.findUserByUsername(request.getExecutorName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getRoleNames().contains("EXECUTOR")) {
                return ResponseEntity.ok(taskService.createTask(request));
            } else
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only executors can have tasks");

        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> optionalTask = taskService.findTaskById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            return ResponseEntity.ok(task);
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
    }

    @GetMapping("/tasks/get-all")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @PostMapping("/tasks/{id}/delete")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        if (taskService.findTaskById(id).isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.ok("Task was deleted");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }
}
