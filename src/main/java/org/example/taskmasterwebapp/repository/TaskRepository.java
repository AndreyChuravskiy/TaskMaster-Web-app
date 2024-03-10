package org.example.taskmasterwebapp.repository;

import org.example.taskmasterwebapp.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
