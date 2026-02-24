package com.syncspace.service;

import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.task.AssignTaskRequest;
import com.syncspace.dto.task.CreateTaskRequest;
import com.syncspace.dto.task.TaskResponse;
import com.syncspace.dto.task.UpdateTaskStatusRequest;
import com.syncspace.entity.TaskStatus;

import java.util.UUID;

public interface TaskService {

    TaskResponse createTask(UUID projectId, CreateTaskRequest request);

    TaskResponse getTaskById(UUID taskId);

    TaskResponse updateTaskStatus(UUID taskId, UpdateTaskStatusRequest request);

    TaskResponse assignTask(UUID taskId, AssignTaskRequest request);

    PageResponse<TaskResponse> listTasks(TaskStatus status, int page, int size);

    void softDeleteTask(UUID taskId);
}