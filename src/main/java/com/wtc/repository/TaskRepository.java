package com.wtc.repository;

import com.wtc.entity.Task;
import com.wtc.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status);
}
