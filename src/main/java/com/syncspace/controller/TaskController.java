package com.syncspace.controller;

import com.syncspace.dto.common.ApiResponse;
import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.task.AssignTaskRequest;
import com.syncspace.dto.task.CreateTaskRequest;
import com.syncspace.dto.task.TaskResponse;
import com.syncspace.dto.task.UpdateTaskStatusRequest;
import com.syncspace.entity.TaskStatus;
import com.syncspace.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/projects/{projectId}/tasks")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<TaskResponse> createTask(
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        return ApiResponse.success("Task created", taskService.createTask(projectId, request));
    }

    @GetMapping("/tasks/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<TaskResponse> getTaskById(@PathVariable UUID taskId) {
        return ApiResponse.success("Task fetched", taskService.getTaskById(taskId));
    }

    @PatchMapping("/tasks/{taskId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<TaskResponse> updateTaskStatus(
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        return ApiResponse.success("Task status updated", taskService.updateTaskStatus(taskId, request));
    }

    @PatchMapping("/tasks/{taskId}/assignee")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<TaskResponse> assignTask(
            @PathVariable UUID taskId,
            @Valid @RequestBody AssignTaskRequest request
    ) {
        return ApiResponse.success("Task assignee updated", taskService.assignTask(taskId, request));
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<PageResponse<TaskResponse>> listTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success("Task list fetched", taskService.listTasks(status, page, size));
    }

    @DeleteMapping("/tasks/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> softDeleteTask(@PathVariable UUID taskId) {
        taskService.softDeleteTask(taskId);
        return ApiResponse.success("Task deleted", null);
    }
}