package com.syncspace.controller;

import com.syncspace.dto.activity.ActivityLogResponse;
import com.syncspace.dto.common.ApiResponse;
import com.syncspace.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping("/workspaces/{workspaceId}/activity-logs")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<List<ActivityLogResponse>> listByWorkspace(@PathVariable UUID workspaceId) {
        return ApiResponse.success("Workspace activity logs fetched", activityLogService.listByWorkspace(workspaceId));
    }

    @GetMapping("/projects/{projectId}/activity-logs")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<List<ActivityLogResponse>> listByProject(@PathVariable UUID projectId) {
        return ApiResponse.success("Project activity logs fetched", activityLogService.listByProject(projectId));
    }
}