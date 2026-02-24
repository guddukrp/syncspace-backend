package com.syncspace.service;

import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.workspace.CreateWorkspaceRequest;
import com.syncspace.dto.workspace.WorkspaceResponse;

import java.util.UUID;

public interface WorkspaceService {

    WorkspaceResponse createWorkspace(CreateWorkspaceRequest request);

    WorkspaceResponse getWorkspaceById(UUID workspaceId);

    PageResponse<WorkspaceResponse> listWorkspaces(int page, int size);

    void softDeleteWorkspace(UUID workspaceId);
}
