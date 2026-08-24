package com.syncspace.repository;

import com.syncspace.entity.WorkspaceMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, UUID> {

    boolean existsByWorkspaceIdAndUserIdAndDeletedFalse(UUID workspaceId, UUID userId);

    Optional<WorkspaceMember> findByWorkspaceIdAndUserIdAndDeletedFalse(UUID workspaceId, UUID userId);

    Optional<WorkspaceMember> findByIdAndWorkspaceIdAndDeletedFalse(UUID id, UUID workspaceId);

    List<WorkspaceMember> findByWorkspaceIdAndDeletedFalseOrderByCreatedAtAsc(UUID workspaceId);

    Page<WorkspaceMember> findByUserIdAndDeletedFalseAndWorkspaceDeletedFalse(UUID userId, Pageable pageable);
}
