package org.example.taskmasterwebapp.repository;

import org.example.taskmasterwebapp.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByExecutorName(String executorName);

}
