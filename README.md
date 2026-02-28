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
