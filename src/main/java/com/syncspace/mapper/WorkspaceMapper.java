package com.syncspace.mapper;

import com.syncspace.dto.workspace.CreateWorkspaceRequest;
import com.syncspace.dto.workspace.WorkspaceResponse;
import com.syncspace.entity.Workspace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {

    Workspace toEntity(CreateWorkspaceRequest request);

    WorkspaceResponse toResponse(Workspace workspace);
}
