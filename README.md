# 🛡️ Insurance Policy Manager

A fullstack application for managing insurance policies, built with **Spring Boot** (backend) and **React** (frontend), containerised with **Docker Compose**.

---

## Architecture

```
insurance-policy-fullstack/
├── backend/          # Spring Boot 3 · Java 21 · Axon Framework · H2
├── frontend/         # React 18 · Vite · Nginx
├── docker-compose.yml
└── README.md
```

| Service  | Technology                        | Port |
|----------|-----------------------------------|------|
| Backend  | Spring Boot 3, Axon CQRS, H2 DB   | 8080 |
| Frontend | React 18, Vite build, Nginx serve | 3000 |

### Design patterns
- **CQRS** via Axon Framework — commands (create, update) and queries (find) are fully separated
- **Event Sourcing** — aggregate events drive state projection into an H2 read model
- **REST API** — clean JSON contract consumed by the React SPA

---

## Prerequisites

| Tool          | Minimum version |
|---------------|-----------------|
| Docker        | 24+             |
| Docker Compose| 2.20+           |

> No JDK or Node.js required — everything builds inside Docker.

---

## Build & Run

### 1 — Clone  the project

```bash
git clone https://github.com/oussamah21/insurance-policy.git
cd insurance-policy-fullstack
```

### 2 — Start everything with one command

```bash
docker compose up --build
```

Docker will:
1. Compile the Spring Boot JAR inside a JDK container (multi-stage build)
2. Build the React app with Vite and serve it via Nginx
3. Wire both containers on a shared bridge network

> First build takes **3–5 minutes** (Maven dependency download + npm install).
> Subsequent builds are much faster thanks to layer caching.

### 3 — Open the app

| URL                          | Description              |
|------------------------------|--------------------------|
| http://localhost:3000        | React frontend           |
| http://localhost:8080/api/policies | REST API           |
| http://localhost:8080/h2-console   | H2 database console|

---

## API Reference

| Method | Endpoint              | Description           |
|--------|-----------------------|-----------------------|
| GET    | `/api/policies`       | List all policies     |
| GET    | `/api/policies/{id}`  | Get policy by ID      |
| POST   | `/api/policies`       | Create a new policy   |
| PUT    | `/api/policies/{id}`  | Update a policy       |

### Example — Create a policy

```bash
curl -X POST http://localhost:8080/api/policies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Home Insurance Premium",
    "status": "ACTIVE",
    "startDate": "2024-01-01",
    "endDate": "2025-01-01"
  }'
```

### Policy statuses
| Value      | Meaning          |
|------------|------------------|
| `ACTIVE`   | Policy in force  |
| `INACTIVE` | Policy suspended |

---

## Stopping the application

```bash
# Stop containers (keep images)
docker compose down

# Stop and remove everything including images
docker compose down --rmi all --volumes
```

---

## Development (without Docker)

**Backend**
```bash
cd backend
./mvnw spring-boot:run
# API available at http://localhost:8080
```

**Frontend**
```bash
cd frontend
npm install
npm run dev
# UI available at http://localhost:5173
# Proxies /api → http://localhost:8080 via vite.config.js
```
