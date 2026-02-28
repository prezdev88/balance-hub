# Balance Hub

## Conectarse a PostgreSQL (Docker)

```bash
docker exec -it balance-hub-db psql -U balancehub -d balance_hub
```

## Spring profiles

- `dev` (default): usa `application-dev.properties`
- `prod`: usa `application-prod.properties`

### Ejecutar en dev

```bash
./mvnw spring-boot:run
```

### Ejecutar en prod

```bash
SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run
```

### Ejecutar en prod con Docker Compose

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

Variables opcionales en `prod`:

- `DB_URL` (default `jdbc:postgresql://localhost:5434/balance_hub`)
- `DB_USERNAME` (default `balancehub`)
- `DB_PASSWORD` (default `balancehub`)

## Auth y acceso deudores

Backend ahora requiere autenticacion por token Bearer para `/api/**`.

### Login

`POST /api/auth/login`

Body:

```json
{
  "email": "admin@balancehub.local",
  "password": "Admin1234"
}
```

Respuesta: `accessToken`, `expiresAt`, `role`, `debtorId`.

### Bootstrap admin

Si no existe ningun usuario `ADMIN`, al iniciar la app se crea uno automaticamente.

Variables opcionales:

- `app.admin.email` (default `admin@balancehub.local`)
- `app.admin.password` (default `Admin1234`)

### Gestion acceso deudor (solo ADMIN)

- `POST /api/admin/debtors/{debtorId}/access/grant`
  - body opcional `{ "password": "..." }`
  - si no se envia password, genera una simple automaticamente.
- `POST /api/admin/debtors/{debtorId}/access/password`
  - cambia o genera nueva password.
- `POST /api/admin/debtors/{debtorId}/access/revoke`
  - revoca acceso de login para ese deudor.
