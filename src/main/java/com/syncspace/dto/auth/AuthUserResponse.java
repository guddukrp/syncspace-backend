package com.syncspace.dto.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AuthUserResponse {
    private UUID id;
    private String email;
    private String displayName;
    private String role;
}