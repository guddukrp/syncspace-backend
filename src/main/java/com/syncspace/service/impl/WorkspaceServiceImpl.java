package com.syncspace.service.impl;

import com.syncspace.dto.common.PageResponse;
import com.syncspace.dto.workspace.CreateWorkspaceRequest;
import com.syncspace.dto.workspace.WorkspaceResponse;
import com.syncspace.entity.Workspace;
import com.syncspace.entity.WorkspaceMember;
import com.syncspace.exception.UnauthorizedException;
import com.syncspace.exception.NotFoundException;
import com.syncspace.mapper.WorkspaceMapper;
import com.syncspace.repository.WorkspaceMemberRepository;
import com.syncspace.repository.WorkspaceRepository;
import com.syncspace.service.WorkspaceAuthorizationService;
import com.syncspace.service.WorkspaceService;
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
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceAuthorizationService workspaceAuthorizationService;

    @Override
    @Transactional
    public WorkspaceResponse createWorkspace(CreateWorkspaceRequest request) {
        UUID currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new UnauthorizedException("Authentication required");
        }

        Workspace workspace = workspaceMapper.toEntity(request);
        workspace.setOwnerId(currentUserId);
        Workspace saved = workspaceRepository.save(workspace);

        WorkspaceMember ownerMembership = new WorkspaceMember();
        ownerMembership.setWorkspace(saved);
        ownerMembership.setUserId(currentUserId);
        ownerMembership.setRole("OWNER");
        workspaceMemberRepository.save(ownerMembership);

        log.info("Workspace created: {}", saved.getId());
        return workspaceMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkspaceResponse getWorkspaceById(UUID workspaceId) {
        Workspace workspace = workspaceRepository.findByIdAndDeletedFalse(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));
        workspaceAuthorizationService.requireWorkspaceMember(workspaceId);
        return workspaceMapper.toResponse(workspace);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WorkspaceResponse> listWorkspaces(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WorkspaceResponse> mappedPage;

        if (workspaceAuthorizationService.isCurrentUserAdmin()) {
            mappedPage = workspaceRepository.findByDeletedFalse(pageable).map(workspaceMapper::toResponse);
        } else {
            UUID currentUserId = SecurityUtil.getCurrentUserId();
            if (currentUserId == null) {
                throw new UnauthorizedException("Authentication required");
            }

            mappedPage = workspaceMemberRepository.findByUserIdAndDeletedFalseAndWorkspaceDeletedFalse(currentUserId, pageable)
                    .map(WorkspaceMember::getWorkspace)
                    .map(workspaceMapper::toResponse);
        }

        return PageResponseUtil.fromPage(mappedPage);
    }

    @Override
    @Transactional
    public void softDeleteWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository.findByIdAndDeletedFalse(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));

        workspaceAuthorizationService.requireWorkspaceManager(workspaceId);
        workspaceRepository.delete(workspace);
        log.info("Workspace soft deleted: {}", workspaceId);
    }
}
