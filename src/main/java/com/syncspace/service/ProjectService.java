package com.syncspace.service;

import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.project.CreateProjectRequest;
import com.syncspace.dto.project.ProjectResponse;

import java.util.UUID;

public interface ProjectService {

    ProjectResponse createProject(UUID workspaceId, CreateProjectRequest request);

    PageResponse<ProjectResponse> listProjects(UUID workspaceId, int page, int size);

    void softDeleteProject(UUID projectId);
}
