package com.syncspace.service.impl;

import com.syncspace.dto.workspace.AddWorkspaceMemberRequest;
import com.syncspace.dto.workspace.UpdateWorkspaceMemberRoleRequest;
import com.syncspace.dto.workspace.WorkspaceMemberResponse;
import com.syncspace.entity.User;
import com.syncspace.entity.Workspace;
import com.syncspace.entity.WorkspaceMember;
import com.syncspace.exception.BadRequestException;
import com.syncspace.exception.NotFoundException;
import com.syncspace.repository.UserRepository;
import com.syncspace.repository.WorkspaceMemberRepository;
import com.syncspace.repository.WorkspaceRepository;
import com.syncspace.service.WorkspaceAuthorizationService;
import com.syncspace.service.WorkspaceMemberService;
import com.syncspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;
    private final WorkspaceAuthorizationService workspaceAuthorizationService;

    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceMemberResponse> listMembers(UUID workspaceId) {
        Workspace workspace = getWorkspace(workspaceId);
        workspaceAuthorizationService.requireWorkspaceMember(workspace.getId());

        List<WorkspaceMember> members = workspaceMemberRepository
                .findByWorkspaceIdAndDeletedFalseOrderByCreatedAtAsc(workspaceId);
        Map<UUID, User> usersById = userRepository.findAllById(
                        members.stream().map(WorkspaceMember::getUserId).toList()
                )
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return members.stream()
                .map(member -> toResponse(member, usersById.get(member.getUserId())))
                .toList();
    }

    @Override
    @Transactional
    public WorkspaceMemberResponse addMember(UUID workspaceId, AddWorkspaceMemberRequest request) {
        Workspace workspace = getWorkspace(workspaceId);
        workspaceAuthorizationService.requireWorkspaceManager(workspaceId);

        User user = userRepository.findByEmailAndDeletedFalse(normalizeEmail(request.getEmail()))
                .orElseThrow(() -> new NotFoundException("User not found for email: " + request.getEmail()));

        workspaceMemberRepository.findByWorkspaceIdAndUserIdAndDeletedFalse(workspaceId, user.getId())
                .ifPresent(existing -> {
                    throw new BadRequestException("User is already a workspace member");
                });

        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspace(workspace);
        member.setUserId(user.getId());
        member.setRole(normalizeManagedRole(request.getRole()));

        return toResponse(workspaceMemberRepository.save(member), user);
    }

    @Override
    @Transactional
    public WorkspaceMemberResponse updateMemberRole(
            UUID workspaceId,
            UUID memberId,
            UpdateWorkspaceMemberRoleRequest request
    ) {
        workspaceAuthorizationService.requireWorkspaceManager(workspaceId);

        WorkspaceMember member = getActiveMember(workspaceId, memberId);
        ensureOwnerIsNotModified(member);
        member.setRole(normalizeManagedRole(request.getRole()));

        User user = userRepository.findById(member.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + member.getUserId()));
        return toResponse(workspaceMemberRepository.save(member), user);
    }

    @Override
    @Transactional
    public void removeMember(UUID workspaceId, UUID memberId) {
        workspaceAuthorizationService.requireWorkspaceManager(workspaceId);

        WorkspaceMember member = getActiveMember(workspaceId, memberId);
        ensureOwnerIsNotModified(member);

        UUID currentUserId = SecurityUtil.getCurrentUserId();
        if (member.getUserId().equals(currentUserId)) {
            throw new BadRequestException("Workspace managers cannot remove themselves");
        }

        workspaceMemberRepository.delete(member);
    }

    private Workspace getWorkspace(UUID workspaceId) {
        return workspaceRepository.findByIdAndDeletedFalse(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));
    }

    private WorkspaceMember getActiveMember(UUID workspaceId, UUID memberId) {
        return workspaceMemberRepository.findByIdAndWorkspaceIdAndDeletedFalse(memberId, workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace member not found: " + memberId));
    }

    private void ensureOwnerIsNotModified(WorkspaceMember member) {
        if ("OWNER".equalsIgnoreCase(member.getRole())) {
            throw new AccessDeniedException("Workspace owner membership cannot be modified");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String normalizeManagedRole(String role) {
        String normalized = role == null || role.isBlank() ? "MEMBER" : role.trim().toUpperCase();
        if (!"ADMIN".equals(normalized) && !"MEMBER".equals(normalized)) {
            throw new BadRequestException("Role must be ADMIN or MEMBER");
        }
        return normalized;
    }

    private WorkspaceMemberResponse toResponse(WorkspaceMember member, User user) {
        return WorkspaceMemberResponse.builder()
                .id(member.getId())
                .workspaceId(member.getWorkspace().getId())
                .userId(member.getUserId())
                .displayName(user == null ? null : user.getDisplayName())
                .email(user == null ? null : user.getEmail())
                .role(member.getRole())
                .joinedAt(member.getCreatedAt())
                .build();
    }
}
