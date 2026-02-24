package com.syncspace.mapper;

import com.syncspace.dto.task.CreateTaskRequest;
import com.syncspace.dto.task.TaskResponse;
import com.syncspace.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(CreateTaskRequest request);

    @Mapping(target = "projectId", source = "project.id")
    TaskResponse toResponse(Task task);
}
