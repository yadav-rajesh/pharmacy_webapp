# Pharmacy Webapp

Padmavati Medicals website with:

- `backend/`: Spring Boot API
- `frontend/`: React + Vite client

## Local development

Run the backend:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Backend URL: `http://localhost:8080/api/health`

The backend now persists medicine requests by default with a local H2 database and stores uploaded prescription files on disk.

Run the frontend:

```powershell
cd frontend
npm run dev
```

Frontend URL: `http://localhost:5173`

The React app uses the Vite dev proxy, so requests to `/api/*` are forwarded to the Spring Boot server.

## Backend persistence

- Default local storage uses an H2 file database.
- Prescription uploads are stored under the configured `medicine-requests.storage-dir`.
- MySQL profile settings are available in `backend/src/main/resources/application-mysql.properties`.
- PostgreSQL profile settings are available in `backend/src/main/resources/application-postgres.properties`.
- Admin-friendly request endpoints are available at `GET /api/medicine-requests` and `GET /api/medicine-requests/{requestId}`.
- Request statuses currently use `NEW`, `CONTACTED`, and `FULFILLED`.

## Production notes

- The current order flow is WhatsApp-based, so the frontend can be deployed immediately as a static site.
- Canonical URLs, Open Graph URLs, and page schema use `VITE_SITE_URL` when it is set.
- The medicine request form can send multipart requests to `POST /api/medicine-requests`.
- Set `VITE_API_BASE_URL` if your backend is deployed on a different domain from the frontend.
- Copy `frontend/.env.example` to a real environment variable in your hosting dashboard and set it to your live domain.

## Deploy on Vercel

1. Import this GitHub repository into Vercel.
2. Keep the project root at the repo root.
3. Set the environment variable `VITE_SITE_URL` to your final domain.
4. Deploy.

This repo already includes:

- `vercel.json` for building the `frontend/` app from the monorepo
- `frontend/public/_redirects` for SPA routing compatibility on static hosts
- `frontend/public/robots.txt` for crawl access

## Deploy on Netlify

1. Connect the GitHub repository.
2. Set the base directory to `frontend`.
3. Set the build command to `npm run build`.
4. Set the publish directory to `dist`.
5. Add `VITE_SITE_URL` in the site environment variables.

The `_redirects` file is already in `frontend/public/`, so React Router routes will resolve after deployment.
