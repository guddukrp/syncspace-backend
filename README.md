# Syncspace Backend

Production-ready Spring Boot backend scaffold for a collaborative workspace/task platform.

## Tech Stack
- Java 21
- Spring Boot 3.x
- Spring Security 6 (stateless)
- Spring Data JPA + Hibernate
- PostgreSQL
- MapStruct
- Lombok

## Architecture
Layered architecture with packages:
- `config`
- `controller`
- `service`
- `service.impl`
- `repository`
- `entity`
- `dto`
- `mapper`
- `security`
- `exception`
- `util`

## Implemented Modules
1. Workspace module
- Create workspace
- Get workspace by id
- Soft delete workspace
- List workspaces with pagination

2. Project module
- Create project in workspace
- List projects by workspace with pagination
- Soft delete project

3. Task module
- Create task
- Update status
- Assign user
- Filter by status
- List with pagination
- Soft delete task

4. Activity log
- Auto-log on task creation
- Auto-log on status update
- Auto-log on assignment update

5. Security
- Spring Security 6 config
- Stateless session policy
- JWT filter placeholder (`JwtAuthenticationFilter`)
- Method-level authorization with `@PreAuthorize`

## API Response Format
All controllers return:
```json
{
  "success": true,
  "message": "...",
  "data": {}
}
```

## Configuration
Set environment variables:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`
- `SERVER_PORT`

`application.yml` uses:
- `spring.jpa.hibernate.ddl-auto=validate`

## Run Locally
```bash
mvn clean spring-boot:run
```

## Build Docker Image
```bash
docker build -t syncspace:latest .
```

## Run Docker Container
```bash
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host:5432/syncspace \
  -e DB_USERNAME=syncspace_user \
  -e DB_PASSWORD=syncspace_password \
  -e JWT_SECRET=your-secret \
  syncspace:latest
```

## Notes
- JWT parsing in `JwtAuthenticationFilter` is intentionally placeholder logic.
- Replace placeholder token handling with real JWT verification before production deployment.
- Ensure DB schema column names/types match entity mappings exactly because `ddl-auto=validate` is enabled.
