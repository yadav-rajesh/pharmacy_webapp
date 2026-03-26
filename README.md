# Padmavati Medicals

This repository is split into two parts:

- `frontend/` contains the HTML, CSS, JavaScript, and image assets for the pharmacy website.
- `backend/` contains the Spring Boot application that serves the frontend and stores inquiries and medicine order requests.

## Project structure

- `frontend/`
  - `index.html`
  - `about.html`
  - `products.html`
  - `order.html`
  - `contact.html`
  - `styles.css`
  - `script.js`
  - `assets/`
- `backend/`
  - `pom.xml`
  - `src/main/java/...`
  - `src/main/resources/application.properties`

## Backend behavior

The Spring Boot app:

- serves the frontend from `../frontend`
- exposes `GET /api/health`
- saves medicine availability inquiries to `../backend-data/inquiries`
- saves medicine orders to `../backend-data/orders`
- saves uploaded prescriptions to `../backend-data/uploads`

## Run the backend

From the `backend` folder:

```powershell
mvn spring-boot:run
```

Then open:

- `http://localhost:8080/`
- `http://localhost:8080/about`
- `http://localhost:8080/products`
- `http://localhost:8080/order`
- `http://localhost:8080/contact`

## Notes

- The frontend still opens WhatsApp after a successful save, so the local conversion flow stays intact.
- Maven was not available in the current environment, so the Spring Boot build was structured but not executed here.
