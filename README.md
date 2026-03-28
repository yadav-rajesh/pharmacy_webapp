# Pharmacy Webapp

Minimal full-stack starter with:

- `backend/`: Spring Boot API
- `frontend/`: React + Vite client

## Run the backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Backend URL: `http://localhost:8080/api/health`

## Run the frontend

```powershell
cd frontend
npm run dev
```

Frontend URL: `http://localhost:5173`

The React app uses the Vite dev proxy, so requests to `/api/*` are forwarded to the Spring Boot server.
