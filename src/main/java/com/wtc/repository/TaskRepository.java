package com.wtc.repository;

import com.wtc.entity.Task;
import com.wtc.enums.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByAssignedToIdAndStatus(String userId, TaskStatus status);
}
