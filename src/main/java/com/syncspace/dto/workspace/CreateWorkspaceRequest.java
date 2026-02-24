package com.syncspace.dto.workspace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateWorkspaceRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    private UUID ownerId;
}
