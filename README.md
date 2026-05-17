# SPIT Management System

<p align="center">
  <img src="./frontend/public/logo.png" alt="SPIT Management logo" width="120" />
</p>

A full-stack club management system focused on role-based access control, member and semester management, task workflows, and internal notifications.

## Key Features

- JWT authentication (access/refresh token flow)
- Role and permission based authorization
- User, member, board, and semester management
- Task and task request management
- Internal notification management
- Cloudinary avatar upload
- Semester-based dashboard statistics

## Architecture

- `backend`: Spring Boot 3, Java 21, MySQL, Flyway, Spring Security
- `frontend`: Next.js 16, React 19, TypeScript, Ant Design, Tailwind CSS 4
- `compose.yml`: run the full stack with Docker Compose

## Tech Stack

### Backend

- Java 21
- Spring Boot 3.5.6
- Spring Security
- Spring Data JPA
- Flyway
- JWT (jjwt)
- Spring Validation
- Cloudinary
- springdoc OpenAPI (Swagger UI)

### Frontend

- Node.js >= 22
- Next.js 16.1.1
- React 19
- TypeScript
- Ant Design 6
- Tailwind CSS 4
- TanStack Query

### Database

- MySQL 8

## Environment Requirements

- Java 21
- Node.js >= 22
- Docker + Docker Compose (recommended for quick setup)
- MySQL 8 (if running locally without Docker)

## Environment Setup

### Backend

1. Create `.env` from `backend/.env.example`.
2. Configure required variables:
   - `JWT_SECRET`
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `CLOUDINARY_CLOUD_NAME`
   - `CLOUDINARY_API_KEY`
   - `CLOUDINARY_API_SECRET`

### Frontend

1. Create `.env` from `frontend/.env.example`.
2. Make sure `NEXT_PUBLIC_BACKEND_URL` points to your backend.

## Run the Project

### Option 1: Docker Compose (recommended)

```bash
docker compose up -d --build
```

Default service endpoints:

- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080`
- MySQL: `localhost:3303`

### Option 2: Run manually

Backend:

```bash
cd backend
./gradlew bootRun
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

## API Documentation

After starting the backend, open Swagger UI at:

- `http://localhost:8080/swagger-ui/index.html`

## Security

> [!IMPORTANT]
> Never commit `.env`, `.env.production`, JWT secrets, database credentials, or Cloudinary keys.

- The project uses JWT for authentication and Spring Security for authorization.
- User passwords are hashed with BCrypt before storage.
- Refresh tokens are stored to manage login sessions.
- Use strong secrets, rotate them regularly, and separate secrets by environment.

If you discover a security issue:

- Do not open a public issue with exploit details.
- Report it privately through the project’s internal communication channel.
- See `SECURITY.md` for details.

## License

This project is released under the MIT License.

- Full text is available in `LICENSE`.
