# Syncspace Project Notes

This repository contains the Syncspace full-stack project context from the earlier build session.

The project was created as two separate apps:

- `syncspace-backend`: Spring Boot backend API
- `syncspace-frontend`: React + TypeScript frontend

## Original Goal

Build a production-ready workspace/project/task management application named `syncspace`.

The backend was requested first as a Java 21 Spring Boot project using PostgreSQL, layered architecture, JPA/Hibernate, DTOs, MapStruct, Lombok, Spring Security 6, JWT auth, validation, global exception handling, standard API responses, tests, Dockerfile, and deployment-ready configuration.

The frontend was then requested as a Vite React + TypeScript app using React Router v6, Axios, TanStack React Query, Zod, React Hook Form, Context API auth state, CSS files, ESLint, Prettier, protected routes, reusable layout/components, and real backend integration without mock data.

## Backend Summary

Location:

```text
syncspace-backend/
```

Main stack:

- Java 21
- Spring Boot 3.3.8
- Spring Security 6
- Spring Data JPA + Hibernate
- PostgreSQL
- Lombok
- MapStruct
- JJWT
- Maven

Important packages:

```text
src/main/java/com/syncspace/
  config/
  controller/
  service/
  service/impl/
  repository/
  entity/
  dto/
  mapper/
  security/
  exception/
  util/
```

Implemented backend modules:

- Workspace create, get by ID, paginated list, soft delete
- Project create inside workspace, paginated list, soft delete
- Task create, update status, assign user, filter by status, paginated list, soft delete
- Activity logs for task creation, status update, and assignee change
- User registration and login
- JWT generation and validation
- CORS support for the Vite frontend
- Standard API response wrapper
- Global exception handling
- Test skeletons
- Dockerfile

Important later backend additions:

- Added persistent `users` table support.
- Added auth DTOs, repository, service, and controller.
- Replaced the initial JWT placeholder with signed JWT handling.
- Added missing frontend-facing endpoints:
  - `GET /tasks/{taskId}`
  - workspace/project activity log APIs
- Added CORS for local frontend origin `http://localhost:5173`.
- Local development was adjusted to allow DB auto-update for the new user table.

Common backend environment variables:

```env
DB_URL=jdbc:postgresql://localhost:5432/syncspace
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=replace-with-a-strong-secret
JWT_EXPIRATION_MS=3600000
SERVER_PORT=8080
```

Run backend locally:

```bash
cd syncspace-backend
./mvnw spring-boot:run
```

Windows:

```bash
cd syncspace-backend
mvnw.cmd spring-boot:run
```

Build backend:

```bash
cd syncspace-backend
./mvnw clean package -DskipTests
```

Windows:

```bash
cd syncspace-backend
mvnw.cmd clean package -DskipTests
```

## Frontend Summary

Location:

```text
syncspace-frontend/
```

Main stack:

- React 19
- TypeScript
- Vite
- React Router v6
- Axios
- TanStack React Query
- Zod
- React Hook Form
- Context API
- ESLint
- Prettier

Requested frontend structure:

```text
src/
  api/
  components/
  components/layout/
  components/common/
  pages/
  hooks/
  services/
  context/
  types/
  routes/
  utils/
  constants/
```

Later frontend refactor:

- Routing logic was moved into `src/App.tsx`.
- The `src/routes/` folder was deleted.
- Constants were consolidated into `src/constant.ts`.
- The `src/constants/` folder was deleted.
- Frontend was wired to the local backend at `http://localhost:8080`.

Implemented frontend features:

- Login page
- Register/create account page
- Auth context with login/logout
- JWT stored in `localStorage`
- Protected routes
- Axios instance with JWT interceptor
- Global `401` handling
- Sidebar, header, main app layout
- Dashboard
- Workspaces list, pagination, create modal, soft delete
- Workspace detail page
- Projects list, create project, pagination
- Project detail page
- Tasks list, status filter, create task, update status, assign user, pagination
- Task detail page
- Activity log panel
- Error boundary
- Reusable loading spinner
- React Query service hooks for API calls

Frontend environment:

```env
VITE_API_URL=http://localhost:8080
```

Run frontend locally:

```bash
cd syncspace-frontend
npm install
npm run dev
```

Local frontend URL:

```text
http://localhost:5173
```

Build frontend:

```bash
cd syncspace-frontend
npm run build
```

## Local Development URLs

Backend:

```text
http://localhost:8080
```

Frontend:

```text
http://localhost:5173
```

The frontend should use:

```env
VITE_API_URL=http://localhost:8080
```

The backend CORS config must allow:

```text
http://localhost:5173
```

## API Response Shape

The backend controllers were designed to return a standard response wrapper:

```json
{
  "success": true,
  "message": "Operation completed",
  "data": {}
}
```

The frontend services expect real backend responses and do not use mock data.

## Deployment Notes

The intended deployment plan from the earlier discussion:

- Deploy backend to Render.
- Deploy frontend to Vercel.
- Use the same source repository if desired, but set each platform's root directory correctly.

### Render Backend

Render service settings:

```text
Root Directory: syncspace-backend
Environment: Java
Build Command: ./mvnw clean package -DskipTests
Start Command: java -jar target/syncspace-backend-0.0.1-SNAPSHOT.jar
```

Render environment variables:

```env
DB_URL=your-render-postgres-jdbc-url
DB_USERNAME=your-db-user
DB_PASSWORD=your-db-password
JWT_SECRET=strong-production-secret
JWT_EXPIRATION_MS=3600000
```

After deployment, copy the Render backend URL, for example:

```text
https://syncspace-backend.onrender.com
```

### Vercel Frontend

Vercel project settings:

```text
Root Directory: syncspace-frontend
Framework Preset: Vite
Build Command: npm run build
Output Directory: dist
```

Vercel environment variable:

```env
VITE_API_URL=https://your-render-backend-url.onrender.com
```

The backend CORS config must include the deployed Vercel frontend URL, for example:

```text
https://your-frontend.vercel.app
```

## Important Reminders

- Restart both servers after changing auth, CORS, or environment variables.
- For production, use a long random `JWT_SECRET`.
- Make sure the PostgreSQL schema matches the backend entity mappings.
- If frontend requests fail in production, check:
  - `VITE_API_URL` in Vercel
  - Render backend logs
  - Browser Network tab
  - Backend CORS allowed origins
  - Whether the JWT token is being sent in the `Authorization` header

## Useful Validation Commands

Backend:

```bash
cd syncspace-backend
./mvnw -DskipTests compile
```

Frontend:

```bash
cd syncspace-frontend
npm run lint
npm run build
```

