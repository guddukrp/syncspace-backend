CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    role VARCHAR(20) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_users_email_active ON users (lower(email)) WHERE deleted = FALSE;
CREATE INDEX idx_users_deleted ON users (deleted);

CREATE TABLE workspaces (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(1000),
    owner_id UUID NOT NULL REFERENCES users (id),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_workspaces_deleted ON workspaces (deleted);
CREATE INDEX idx_workspaces_owner_id ON workspaces (owner_id);

CREATE TABLE workspace_members (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces (id),
    user_id UUID NOT NULL REFERENCES users (id),
    role VARCHAR(50) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_workspace_members_workspace_id ON workspace_members (workspace_id);
CREATE INDEX idx_workspace_members_user_id ON workspace_members (user_id);
CREATE UNIQUE INDEX uk_workspace_members_active_member
    ON workspace_members (workspace_id, user_id)
    WHERE deleted = FALSE;

CREATE TABLE projects (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces (id),
    name VARCHAR(120) NOT NULL,
    description VARCHAR(1000),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_projects_workspace_id_deleted ON projects (workspace_id, deleted);
CREATE INDEX idx_projects_deleted ON projects (deleted);

CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES projects (id),
    title VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(30) NOT NULL DEFAULT 'TODO',
    assignee_id UUID REFERENCES users (id),
    due_date TIMESTAMPTZ,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_tasks_project_id ON tasks (project_id);
CREATE INDEX idx_tasks_deleted ON tasks (deleted);
CREATE INDEX idx_tasks_status_deleted ON tasks (status, deleted);
CREATE INDEX idx_tasks_assignee_id ON tasks (assignee_id);

CREATE TABLE comments (
    id UUID PRIMARY KEY,
    task_id UUID NOT NULL REFERENCES tasks (id),
    author_id UUID NOT NULL REFERENCES users (id),
    content VARCHAR(2000) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_comments_task_id_deleted ON comments (task_id, deleted);
CREATE INDEX idx_comments_author_id ON comments (author_id);

CREATE TABLE activity_logs (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces (id),
    project_id UUID NOT NULL REFERENCES projects (id),
    task_id UUID NOT NULL REFERENCES tasks (id),
    actor_id UUID REFERENCES users (id),
    action VARCHAR(80) NOT NULL,
    details VARCHAR(2000),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_activity_logs_workspace_created_at
    ON activity_logs (workspace_id, created_at DESC);
CREATE INDEX idx_activity_logs_project_created_at
    ON activity_logs (project_id, created_at DESC);
CREATE INDEX idx_activity_logs_task_id ON activity_logs (task_id);
