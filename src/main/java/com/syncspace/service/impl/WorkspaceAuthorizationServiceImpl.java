package com.syncspace.service.impl;

import com.syncspace.entity.WorkspaceMember;
import com.syncspace.exception.UnauthorizedException;
import com.syncspace.repository.WorkspaceMemberRepository;
import com.syncspace.service.WorkspaceAuthorizationService;
import com.syncspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceAuthorizationServiceImpl implements WorkspaceAuthorizationService {

    private static final Set<String> MANAGER_ROLES = Set.of("OWNER", "ADMIN");

    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Override
    public boolean isCurrentUserAdmin() {
        return SecurityUtil.hasRole("ADMIN");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCurrentUserWorkspaceMember(UUID workspaceId) {
        if (isCurrentUserAdmin()) {
            return true;
        }

        UUID userId = getRequiredCurrentUserId();
        return workspaceMemberRepository.existsByWorkspaceIdAndUserIdAndDeletedFalse(workspaceId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canCurrentUserManageWorkspace(UUID workspaceId) {
        if (isCurrentUserAdmin()) {
            return true;
        }

        UUID userId = getRequiredCurrentUserId();
        return workspaceMemberRepository.findByWorkspaceIdAndUserIdAndDeletedFalse(workspaceId, userId)
                .map(WorkspaceMember::getRole)
                .map(String::toUpperCase)
                .filter(MANAGER_ROLES::contains)
                .isPresent();
    }

    @Override
    public void requireWorkspaceMember(UUID workspaceId) {
        if (!isCurrentUserWorkspaceMember(workspaceId)) {
            throw new AccessDeniedException("Workspace access denied");
        }
    }

    @Override
    public void requireWorkspaceManager(UUID workspaceId) {
        if (!canCurrentUserManageWorkspace(workspaceId)) {
            throw new AccessDeniedException("Workspace management access denied");
        }
    }

    private UUID getRequiredCurrentUserId() {
        UUID userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new UnauthorizedException("Authentication required");
        }
        return userId;
    }
}
