package com.syncspace.dto.project;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ProjectResponse {
    private UUID id;
    private UUID workspaceId;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
