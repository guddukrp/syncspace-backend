package com.syncspace.service.impl;

import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.task.AssignTaskRequest;
import com.syncspace.dto.task.CreateTaskRequest;
import com.syncspace.dto.task.TaskResponse;
import com.syncspace.dto.task.UpdateTaskStatusRequest;
import com.syncspace.entity.Project;
import com.syncspace.entity.Task;
import com.syncspace.entity.TaskStatus;
import com.syncspace.exception.NotFoundException;
import com.syncspace.mapper.TaskMapper;
import com.syncspace.repository.ProjectRepository;
import com.syncspace.repository.TaskRepository;
import com.syncspace.service.ActivityLogService;
import com.syncspace.service.TaskService;
import com.syncspace.util.PageResponseUtil;
import com.syncspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;
    private final ActivityLogService activityLogService;

    @Override
    @Transactional
    public TaskResponse createTask(UUID projectId, CreateTaskRequest request) {
        Project project = projectRepository.findByIdAndDeletedFalse(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found: " + projectId));

        Task task = taskMapper.toEntity(request);
        task.setProject(project);

        Task saved = taskRepository.save(task);
        activityLogService.logTaskCreated(saved, SecurityUtil.getCurrentUserId());

        log.info("Task created: {} in project {}", saved.getId(), projectId);
        return taskMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID taskId) {
        return taskMapper.toResponse(getActiveTask(taskId));
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(UUID taskId, UpdateTaskStatusRequest request) {
        Task task = getActiveTask(taskId);
        String previousStatus = task.getStatus().name();

        task.setStatus(request.getStatus());
        Task saved = taskRepository.save(task);

        activityLogService.logTaskStatusUpdated(saved, previousStatus, request.getStatus().name(), SecurityUtil.getCurrentUserId());
        log.info("Task status updated: {} from {} to {}", taskId, previousStatus, request.getStatus());

        return taskMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TaskResponse assignTask(UUID taskId, AssignTaskRequest request) {
        Task task = getActiveTask(taskId);
        UUID previousAssignee = task.getAssigneeId();

        task.setAssigneeId(request.getAssigneeId());
        Task saved = taskRepository.save(task);

        activityLogService.logTaskAssigneeChanged(saved, previousAssignee, request.getAssigneeId(), SecurityUtil.getCurrentUserId());
        log.info("Task assignee updated: {}", taskId);

        return taskMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> listTasks(TaskStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskResponse> mappedPage;

        if (status == null) {
            mappedPage = taskRepository.findByDeletedFalse(pageable).map(taskMapper::toResponse);
        } else {
            mappedPage = taskRepository.findByStatusAndDeletedFalse(status, pageable).map(taskMapper::toResponse);
        }

        return PageResponseUtil.fromPage(mappedPage);
    }

    @Override
    @Transactional
    public void softDeleteTask(UUID taskId) {
        Task task = getActiveTask(taskId);
        taskRepository.delete(task);
        log.info("Task soft deleted: {}", taskId);
    }

    private Task getActiveTask(UUID taskId) {
        return taskRepository.findByIdAndDeletedFalse(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found: " + taskId));
    }
}