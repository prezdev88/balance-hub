# Clean Architecture - Puntos de Mejora

Este documento resume mejoras recomendadas para seguir fortaleciendo la arquitectura del proyecto (`domain` / `application` / `infrastructure`).

## Estado actual (resumen)

El proyecto está bien encaminado hacia Clean Architecture:

- `domain` contiene reglas de negocio y validaciones, sin dependencia de frameworks.
- `application` contiene casos de uso y puertos de salida (`ports/out`).
- `infrastructure` implementa adapters web (controllers) y persistencia (JPA).
- El wiring de casos de uso se hace en `infrastructure/config`, manteniendo `application` limpio de Spring.

## Puntos a mejorar

## 1. Crear `ports/in` (input ports) en `application`

Actualmente los controllers dependen de clases concretas de casos de uso (`CreateDebtorUseCase`, etc.).

### Recomendación

Definir interfaces de entrada en `application/ports/in`, por ejemplo:

- `CreateDebtorInputPort`
- `ListDebtorsInputPort`
- `CreateDebtInputPort`

Luego, los casos de uso implementan esas interfaces y los controllers dependen de las interfaces en lugar de clases concretas.

### Beneficio

- Mejora el desacople entre `infrastructure.web` y `application`.
- Facilita testing/mocking a nivel de controllers.
- Hace más explícita la frontera de entrada del caso de uso.

## 2. Revisar ubicación de `@Transactional`

Ahora varias transacciones están definidas en controllers (`infrastructure/web`), por ejemplo en endpoints de escritura.

### Recomendación

Mover el boundary transaccional a un nivel menos acoplado al transporte HTTP, por ejemplo:

- adapters de aplicación, o
- un orchestration/service de infraestructura dedicado.

### Beneficio

- Evita que la capa web cargue responsabilidades de consistencia transaccional.
- Facilita reutilizar la misma lógica desde otros adapters (CLI, jobs, mensajería).

## 3. Separar DTOs HTTP de DTOs de `application` (opcional, pero recomendable)

Hoy varios controllers devuelven directamente records de `application` (`CreateDebtorResult`, `ListDebtorsResult`, etc.).

### Recomendación

Crear DTOs de transporte en `infrastructure/web`:

- `CreateDebtorHttpResponse`
- `ListDebtorsHttpResponse`
- etc.

y mapear entre DTOs de `application` y DTOs HTTP.

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

## 6. Mantener sincronizados OpenAPI manual y Swagger generado

Actualmente existen dos fuentes útiles:

- `openapi.yaml` (manual, versionado)
- Swagger/OpenAPI generado en runtime (springdoc)

### Riesgo

Pueden divergir si se cambian endpoints/DTOs y no se actualiza `openapi.yaml`.

### Recomendación

Elegir una estrategia explícita:

- **A.** `openapi.yaml` como fuente principal (actualización manual disciplinada)
- **B.** runtime generado como fuente principal y `openapi.yaml` generado automáticamente

## Prioridad sugerida (orden práctico)

1. Tests de integración JPA (infraestructura)
2. ArchUnit (blindaje de arquitectura)
3. `ports/in` en `application`
4. Revisar `@Transactional`
5. Separar DTOs HTTP (si el API empieza a crecer)
6. Definir estrategia única para OpenAPI

