package com.syncspace.repository;

import com.syncspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

    Optional<Workspace> findByIdAndDeletedFalse(UUID id);

    Page<Workspace> findByDeletedFalse(Pageable pageable);
}
