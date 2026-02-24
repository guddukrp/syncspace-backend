package com.syncspace.service.impl;

import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.project.CreateProjectRequest;
import com.syncspace.dto.project.ProjectResponse;
import com.syncspace.entity.Project;
import com.syncspace.entity.Workspace;
import com.syncspace.exception.NotFoundException;
import com.syncspace.mapper.ProjectMapper;
import com.syncspace.repository.ProjectRepository;
import com.syncspace.repository.WorkspaceRepository;
import com.syncspace.service.ProjectService;
import com.syncspace.util.PageResponseUtil;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse createProject(UUID workspaceId, CreateProjectRequest request) {
        Workspace workspace = workspaceRepository.findByIdAndDeletedFalse(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));

        Project project = projectMapper.toEntity(request);
        project.setWorkspace(workspace);

        Project saved = projectRepository.save(project);
        log.info("Project created: {} in workspace {}", saved.getId(), workspaceId);
        return projectMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProjectResponse> listProjects(UUID workspaceId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> mappedPage = projectRepository.findByWorkspaceIdAndDeletedFalse(workspaceId, pageable)
                .map(projectMapper::toResponse);
        return PageResponseUtil.fromPage(mappedPage);
    }

    @Override
    @Transactional
    public void softDeleteProject(UUID projectId) {
        Project project = projectRepository.findByIdAndDeletedFalse(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found: " + projectId));

        projectRepository.delete(project);
        log.info("Project soft deleted: {}", projectId);
    }
}
