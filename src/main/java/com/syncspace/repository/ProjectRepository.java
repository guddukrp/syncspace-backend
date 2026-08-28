package com.syncspace.repository;

import com.syncspace.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Optional<Project> findByIdAndDeletedFalse(UUID id);

    Page<Project> findByDeletedFalse(Pageable pageable);

    Page<Project> findByWorkspaceIdAndDeletedFalse(UUID workspaceId, Pageable pageable);

    Page<Project> findByWorkspaceIdInAndDeletedFalse(Iterable<UUID> workspaceIds, Pageable pageable);
}
