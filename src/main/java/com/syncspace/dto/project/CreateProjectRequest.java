package com.syncspace.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 1000)
    private String description;
}
