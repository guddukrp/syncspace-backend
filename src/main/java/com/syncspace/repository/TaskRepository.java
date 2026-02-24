package com.syncspace.repository;

import com.syncspace.entity.Task;
import com.syncspace.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByIdAndDeletedFalse(UUID id);

    Page<Task> findByDeletedFalse(Pageable pageable);

    Page<Task> findByStatusAndDeletedFalse(TaskStatus status, Pageable pageable);
}
