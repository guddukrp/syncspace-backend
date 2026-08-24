package com.syncspace.dto.workspace;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WorkspaceMemberResponse {
    private UUID id;
    private UUID workspaceId;
    private UUID userId;
    private String displayName;
    private String email;
    private String role;
    private Instant joinedAt;
}
