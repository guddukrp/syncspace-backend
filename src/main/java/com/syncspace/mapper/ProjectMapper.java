package com.syncspace.mapper;

import com.syncspace.dto.project.CreateProjectRequest;
import com.syncspace.dto.project.ProjectResponse;
import com.syncspace.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toEntity(CreateProjectRequest request);

    @Mapping(target = "workspaceId", source = "workspace.id")
    ProjectResponse toResponse(Project project);
}
