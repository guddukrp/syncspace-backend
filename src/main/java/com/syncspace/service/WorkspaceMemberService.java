package com.syncspace.service;

import com.syncspace.dto.workspace.AddWorkspaceMemberRequest;
import com.syncspace.dto.workspace.UpdateWorkspaceMemberRoleRequest;
import com.syncspace.dto.workspace.WorkspaceMemberResponse;

import java.util.List;
import java.util.UUID;

public interface WorkspaceMemberService {

    List<WorkspaceMemberResponse> listMembers(UUID workspaceId);

    WorkspaceMemberResponse addMember(UUID workspaceId, AddWorkspaceMemberRequest request);

    WorkspaceMemberResponse updateMemberRole(
            UUID workspaceId,
            UUID memberId,
            UpdateWorkspaceMemberRoleRequest request
    );

    void removeMember(UUID workspaceId, UUID memberId);
}
