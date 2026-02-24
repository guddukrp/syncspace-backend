package com.syncspace.controller;

import com.syncspace.dto.common.ApiResponse;
import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.project.CreateProjectRequest;
import com.syncspace.dto.project.ProjectResponse;
import com.syncspace.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/workspaces/{workspaceId}/projects")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<ProjectResponse> createProject(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody CreateProjectRequest request
    ) {
        return ApiResponse.success("Project created", projectService.createProject(workspaceId, request));
    }

    @GetMapping("/workspaces/{workspaceId}/projects")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<PageResponse<ProjectResponse>> listProjects(
            @PathVariable UUID workspaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success("Project list fetched", projectService.listProjects(workspaceId, page, size));
    }

    @DeleteMapping("/projects/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> softDeleteProject(@PathVariable UUID projectId) {
        projectService.softDeleteProject(projectId);
        return ApiResponse.success("Project deleted", null);
    }
}
