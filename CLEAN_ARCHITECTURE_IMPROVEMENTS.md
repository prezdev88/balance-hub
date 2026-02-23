# Clean Architecture - Puntos de Mejora

Este documento resume mejoras recomendadas para seguir fortaleciendo la arquitectura del proyecto (`domain` / `application` / `infrastructure`).

## Estado actual (resumen)

El proyecto está bien encaminado hacia Clean Architecture:

- `domain` contiene reglas de negocio y validaciones, sin dependencia de frameworks.
- `application` contiene casos de uso y puertos de salida (`ports/out`).
- `infrastructure` implementa adapters web (controllers) y persistencia (JPA).
- El wiring de casos de uso se hace en `infrastructure/config`, manteniendo `application` limpio de Spring.

## Puntos a mejorar

## 1. Crear `ports/in` (input ports) en `application` (resuelto)

Se implementaron interfaces de entrada en `application/ports/in` y los controllers ahora dependen de esas interfaces en lugar de clases concretas.

### Recomendación

Se definieron interfaces de entrada en `application/ports/in`, por ejemplo:

- `CreateDebtorInputPort`
- `ListDebtorsInputPort`
- `CreateDebtInputPort`

Los casos de uso implementan esas interfaces y los controllers dependen de las interfaces.

### Beneficio

- Mejora el desacople entre `infrastructure.web` y `application`.
- Facilita testing/mocking a nivel de controllers.
- Hace más explícita la frontera de entrada del caso de uso.

## 2. Revisar ubicación de `@Transactional` (resuelto)

El boundary transaccional se movió fuera de los controllers. La capa web ya no usa `@Transactional`.

### Recomendación

El boundary transaccional quedó en `infrastructure/config` usando `TransactionTemplate`, envolviendo casos de uso de escritura.

### Beneficio

- Evita que la capa web cargue responsabilidades de consistencia transaccional.
- Facilita reutilizar la misma lógica desde otros adapters (CLI, jobs, mensajería).

## 3. Separar DTOs HTTP de DTOs de `application` (resuelto)

Los controllers ya no devuelven records de `application` directamente. Se agregaron DTOs HTTP (`record`) en `infrastructure/web` con mapeo explícito desde los resultados de `application`.

### Recomendación

Se crearon DTOs de transporte en `infrastructure/web`, por ejemplo:

- `CreateDebtorHttpResponse`
- `ListDebtorsHttpResponse`
- etc.

y se mapea entre DTOs de `application` y DTOs HTTP.

### Beneficio

- Desacopla el contrato HTTP de la forma interna de los use cases.
- Permite evolucionar la API sin tocar `application`.
- Hace más clara la responsabilidad de cada capa.

## 4. Agregar tests de arquitectura (ArchUnit)

Actualmente la separación está bien por convención, pero no hay reglas automáticas que la protejan.

### Recomendación

Agregar tests con ArchUnit para validar reglas como:

- `domain` no depende de `application` ni `infrastructure`
- `application` no depende de `infrastructure`
- `infrastructure` puede depender de `application` y `domain`
- Solo `infrastructure` usa Spring/JPA (`org.springframework`, `jakarta.persistence`)

### Beneficio

- Evita regresiones de arquitectura a medida que crece el proyecto.
- Hace visible cualquier violación en CI.

## 5. Tests de integración para infraestructura JPA

La capa `infrastructure/persistence/jpa` hoy debería validarse con tests de integración (no solo unitarios).

### Recomendación

Agregar tests con:

- `@DataJpaTest`
- Testcontainers + PostgreSQL

Cubrir al menos:

- adapters JPA críticos
- queries derivadas por rango de fechas
- `@Query` de agregación (`sumAmountByType`)
- `deactivateCurrentSalary()`

### Beneficio

- Valida mappings reales, queries y comportamiento contra PostgreSQL.
- Reduce riesgo de errores que no aparecen con mocks.

## 6. Definir una única fuente oficial para OpenAPI (resuelto)

Se decidió usar **Swagger/OpenAPI generado en runtime (springdoc)** como fuente oficial.

### Decisión adoptada

- El archivo `openapi.yaml` manual se elimina para evitar duplicidad y drift.
- La documentación oficial queda disponible desde:
  - `/v3/api-docs`
  - `/swagger-ui.html` (o `/swagger-ui/index.html`)

### Beneficio

- Se evita mantener dos contratos en paralelo.
- La documentación refleja automáticamente los endpoints/DTOs reales expuestos por el backend.

## Prioridad sugerida (pendientes)

1. Tests de integración JPA (infraestructura)
2. ArchUnit (blindaje de arquitectura)
