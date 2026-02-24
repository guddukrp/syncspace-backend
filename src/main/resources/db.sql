-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.activity_logs (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  task_id uuid,
  project_id uuid,
  workspace_id uuid,
  action character varying NOT NULL,
  description text,
  performed_by uuid NOT NULL,
  created_at timestamp with time zone DEFAULT now(),
  CONSTRAINT activity_logs_pkey PRIMARY KEY (id)
);
CREATE TABLE public.comments (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  task_id uuid NOT NULL,
  content text NOT NULL,
  created_by uuid NOT NULL,
  created_at timestamp with time zone DEFAULT now(),
  CONSTRAINT comments_pkey PRIMARY KEY (id),
  CONSTRAINT fk_task_comment FOREIGN KEY (task_id) REFERENCES public.tasks(id)
);
CREATE TABLE public.projects (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  workspace_id uuid NOT NULL,
  name character varying NOT NULL,
  description text,
  created_by uuid NOT NULL,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  is_deleted boolean DEFAULT false,
  CONSTRAINT projects_pkey PRIMARY KEY (id),
  CONSTRAINT fk_workspace_project FOREIGN KEY (workspace_id) REFERENCES public.workspaces(id)
);
CREATE TABLE public.tasks (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  project_id uuid NOT NULL,
  title character varying NOT NULL,
  description text,
  status USER-DEFINED DEFAULT 'TODO'::task_status,
  priority USER-DEFINED DEFAULT 'MEDIUM'::task_priority,
  assigned_user_id uuid,
  due_date timestamp with time zone,
  created_by uuid NOT NULL,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  is_deleted boolean DEFAULT false,
  CONSTRAINT tasks_pkey PRIMARY KEY (id),
  CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES public.projects(id)
);
CREATE TABLE public.workspace_members (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  workspace_id uuid NOT NULL,
  user_id uuid NOT NULL,
  role USER-DEFINED NOT NULL,
  joined_at timestamp with time zone DEFAULT now(),
  CONSTRAINT workspace_members_pkey PRIMARY KEY (id),
  CONSTRAINT fk_workspace FOREIGN KEY (workspace_id) REFERENCES public.workspaces(id)
);
CREATE TABLE public.workspaces (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  name character varying NOT NULL,
  description text,
  created_by uuid NOT NULL,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  is_deleted boolean DEFAULT false,
  CONSTRAINT workspaces_pkey PRIMARY KEY (id)
);