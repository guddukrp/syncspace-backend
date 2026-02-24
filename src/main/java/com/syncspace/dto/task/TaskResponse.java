package com.syncspace.dto.task;

import com.syncspace.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class TaskResponse {
    private UUID id;
    private UUID projectId;
    private String title;
    private String description;
    private TaskStatus status;
    private UUID assigneeId;
    private Instant dueDate;
    private Instant createdAt;
    private Instant updatedAt;
}
