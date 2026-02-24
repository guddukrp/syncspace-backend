package com.syncspace.dto.task;

import com.syncspace.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatusRequest {

    @NotNull
    private TaskStatus status;
}
