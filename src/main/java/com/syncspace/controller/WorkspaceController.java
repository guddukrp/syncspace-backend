package com.syncspace.controller;

import com.syncspace.dto.common.ApiResponse;
import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.workspace.CreateWorkspaceRequest;
import com.syncspace.dto.workspace.WorkspaceResponse;
import com.syncspace.service.WorkspaceService;
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
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<WorkspaceResponse> createWorkspace(@Valid @RequestBody CreateWorkspaceRequest request) {
        return ApiResponse.success("Workspace created", workspaceService.createWorkspace(request));
    }

    @GetMapping("/{workspaceId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<WorkspaceResponse> getWorkspaceById(@PathVariable UUID workspaceId) {
        return ApiResponse.success("Workspace fetched", workspaceService.getWorkspaceById(workspaceId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<PageResponse<WorkspaceResponse>> listWorkspaces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success("Workspace list fetched", workspaceService.listWorkspaces(page, size));
    }

    @DeleteMapping("/{workspaceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> softDeleteWorkspace(@PathVariable UUID workspaceId) {
        workspaceService.softDeleteWorkspace(workspaceId);
        return ApiResponse.success("Workspace deleted", null);
    }
}
