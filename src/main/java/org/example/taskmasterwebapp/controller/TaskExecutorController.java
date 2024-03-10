package org.example.taskmasterwebapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/tasks")
@RestController
public class TaskExecutorController {

    public String getTask() {
        return "ok";
    }

    public String markAsComplete() {
        return "ok";
    }

    public String getAllTasks() {
        return "ok";
    }

}
