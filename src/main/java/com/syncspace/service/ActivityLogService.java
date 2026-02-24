package com.syncspace.service;

import com.syncspace.dto.activity.ActivityLogResponse;
import com.syncspace.entity.Task;

import java.util.List;
import java.util.UUID;

public interface ActivityLogService {

    void logTaskCreated(Task task, UUID actorId);

    void logTaskStatusUpdated(Task task, String previousStatus, String updatedStatus, UUID actorId);

    void logTaskAssigneeChanged(Task task, UUID previousAssignee, UUID updatedAssignee, UUID actorId);

    List<ActivityLogResponse> listByWorkspace(UUID workspaceId);

    List<ActivityLogResponse> listByProject(UUID projectId);
}