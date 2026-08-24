package com.syncspace.controller;

import com.syncspace.dto.common.ApiResponse;
import com.syncspace.dto.workspace.AddWorkspaceMemberRequest;
import com.syncspace.dto.workspace.UpdateWorkspaceMemberRoleRequest;
import com.syncspace.dto.workspace.WorkspaceMemberResponse;
import com.syncspace.service.WorkspaceMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workspaces/{workspaceId}/members")
@RequiredArgsConstructor
public class WorkspaceMemberController {

    private final WorkspaceMemberService workspaceMemberService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<List<WorkspaceMemberResponse>> listMembers(@PathVariable UUID workspaceId) {
        return ApiResponse.success("Workspace members fetched", workspaceMemberService.listMembers(workspaceId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<WorkspaceMemberResponse> addMember(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody AddWorkspaceMemberRequest request
    ) {
        return ApiResponse.success("Workspace member added", workspaceMemberService.addMember(workspaceId, request));
    }

    @PatchMapping("/{memberId}/role")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<WorkspaceMemberResponse> updateRole(
            @PathVariable UUID workspaceId,
            @PathVariable UUID memberId,
            @Valid @RequestBody UpdateWorkspaceMemberRoleRequest request
    ) {
        return ApiResponse.success(
                "Workspace member role updated",
                workspaceMemberService.updateMemberRole(workspaceId, memberId, request)
        );
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<Void> removeMember(
            @PathVariable UUID workspaceId,
            @PathVariable UUID memberId
    ) {
        workspaceMemberService.removeMember(workspaceId, memberId);
        return ApiResponse.success("Workspace member removed", null);
    }
}
