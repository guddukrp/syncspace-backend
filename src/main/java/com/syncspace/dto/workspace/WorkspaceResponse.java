package com.syncspace.dto.workspace;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class WorkspaceResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID ownerId;
    private Instant createdAt;
    private Instant updatedAt;
}
