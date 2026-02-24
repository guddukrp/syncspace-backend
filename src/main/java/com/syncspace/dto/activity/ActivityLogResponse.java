package com.syncspace.dto.activity;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class ActivityLogResponse {
    private UUID id;
    private UUID taskId;
    private String action;
    private String details;
    private UUID actorId;
    private Instant createdAt;
}
