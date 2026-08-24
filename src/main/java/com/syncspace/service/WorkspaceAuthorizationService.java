package com.syncspace.service;

import java.util.UUID;

public interface WorkspaceAuthorizationService {

    boolean isCurrentUserAdmin();

    boolean isCurrentUserWorkspaceMember(UUID workspaceId);

    boolean canCurrentUserManageWorkspace(UUID workspaceId);

    void requireWorkspaceMember(UUID workspaceId);

    void requireWorkspaceManager(UUID workspaceId);
}
