package com.syncspace.service.impl;

import com.syncspace.dto.activity.ActivityLogResponse;
import com.syncspace.entity.ActivityLog;
import com.syncspace.entity.Task;
import com.syncspace.repository.ActivityLogRepository;
import com.syncspace.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Override
    @Transactional
    public void logTaskCreated(Task task, UUID actorId) {
        saveLog(task, "TASK_CREATED", "Task created", actorId);
    }

    @Override
    @Transactional
    public void logTaskStatusUpdated(Task task, String previousStatus, String updatedStatus, UUID actorId) {
        saveLog(task, "TASK_STATUS_UPDATED", "Status changed from " + previousStatus + " to " + updatedStatus, actorId);
    }

    @Override
    @Transactional
    public void logTaskAssigneeChanged(Task task, UUID previousAssignee, UUID updatedAssignee, UUID actorId) {
        saveLog(task, "TASK_ASSIGNEE_UPDATED", "Assignee changed from " + previousAssignee + " to " + updatedAssignee, actorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogResponse> listByWorkspace(UUID workspaceId) {
        return activityLogRepository.findByWorkspaceIdOrderByCreatedAtDesc(workspaceId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogResponse> listByProject(UUID projectId) {
        return activityLogRepository.findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void saveLog(Task task, String action, String details, UUID actorId) {
        ActivityLog logEntry = new ActivityLog();
        logEntry.setWorkspace(task.getProject().getWorkspace());
        logEntry.setProject(task.getProject());
        logEntry.setTask(task);
        logEntry.setAction(action);
        logEntry.setDetails(details);
        logEntry.setActorId(actorId);

        activityLogRepository.save(logEntry);
        log.debug("Activity log created for task {} with action {}", task.getId(), action);
    }

    private ActivityLogResponse toResponse(ActivityLog logEntry) {
        return ActivityLogResponse.builder()
                .id(logEntry.getId())
                .taskId(logEntry.getTask().getId())
                .action(logEntry.getAction())
                .details(logEntry.getDetails())
                .actorId(logEntry.getActorId())
                .createdAt(logEntry.getCreatedAt())
                .build();
    }
}