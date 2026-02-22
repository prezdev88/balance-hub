-- Balance Hub - Consultas PostgreSQL por caso de uso
-- Objetivo: servir como base para implementar adapters/repositories de infrastructure.
-- Formato de parámetros: $1, $2... (estilo PostgreSQL).

/* ============================================================================
   DEBTOR / CreateDebtorUseCase
   Fuente: application/usecases/debtor/create/CreateDebtorUseCase
   ============================================================================ */

-- Inserta un deudor.
-- Params:
--   $1 = id (UUID string)
--   $2 = name
--   $3 = email
INSERT INTO debtors (id, name, email)
VALUES ($1, $2, $3);


/* ============================================================================
   DEBTOR / ListDebtorsUseCase
   Fuente: application/usecases/debtor/list/ListDebtorsUseCase
   ============================================================================ */

-- Lista todos los deudores.
-- El caso de uso no exige orden, pero ordenar facilita tests y consistencia.
SELECT id, name, email
FROM debtors
ORDER BY name ASC, id ASC;


/* ============================================================================
   DEBT / CreateDebtUseCase
   Fuente: application/usecases/debt/create/CreateDebtUseCase
   Notas:
   - El caso de uso crea deuda + N cuotas.
   - Recomendado ejecutarlo en una transacción.
   ============================================================================ */

BEGIN;

-- 1) Inserta deuda.
-- Params:
--   $1 = debt.id
--   $2 = debt.description
--   $3 = debt.total_amount
--   $4 = debt.debtor_id
--   $5 = debt.created_at
--   $6 = debt.settled (false al crear)
INSERT INTO debts (id, description, total_amount, debtor_id, created_at, settled)
VALUES ($1, $2, $3, $4, $5, $6);

-- 2) Inserta cuotas (una por fila).
-- Params por fila:
--   $1 = installment.id
--   $2 = debt_id
--   $3 = installment_no
--   $4 = due_date
--   $5 = paid_at (NULL al crear)
--   $6 = amount
INSERT INTO installments (id, debt_id, installment_no, due_date, paid_at, amount)
VALUES ($1, $2, $3, $4, $5, $6);

COMMIT;

-- Variante batch (más eficiente) si tu adapter construye un multi-values:
-- INSERT INTO installments (id, debt_id, installment_no, due_date, paid_at, amount)
-- VALUES
--   ($1, $2, $3, $4, $5, $6),
--   ($7, $8, $9, $10, $11, $12),
--   ...
-- ;


/* ============================================================================
   DEBT / GetDebtsUseCase
   Fuente: application/usecases/debt/get/GetDebtsUseCase
   ============================================================================ */

-- 1) Busca deudor por id (para validar existencia y construir respuesta).
-- Params:
--   $1 = debtor_id
SELECT id, name, email
FROM debtors
WHERE id = $1;

-- 2) Busca deudas por debtor + rango.
-- Opción A (TIMESTAMPTZ puro): [startDate 00:00, endDate + 1 día)
-- Params:
--   $1 = debtor_id
--   $2 = start_date::date
--   $3 = end_date::date
SELECT id, description, total_amount, debtor_id, created_at, settled
FROM debts
WHERE debtor_id = $1
  AND created_at >= ($2::date)::timestamptz
  AND created_at < (($3::date + INTERVAL '1 day'))::timestamptz
ORDER BY created_at DESC, id ASC;

-- Opción B (más simple, usando cast a date):
-- SELECT id, description, total_amount, debtor_id, created_at, settled
-- FROM debts
-- WHERE debtor_id = $1
--   AND created_at::date BETWEEN $2::date AND $3::date
-- ORDER BY created_at DESC, id ASC;

-- 3) Busca cuotas por lista de debt ids.
-- Params:
--   $1 = array de ids (text[])
SELECT id, debt_id, installment_no, due_date, paid_at, amount
FROM installments
WHERE debt_id = ANY ($1::text[])
ORDER BY debt_id ASC, installment_no ASC;


/* ============================================================================
   INSTALLMENT / PayInstallmentUseCase
   Fuente: application/usecases/installment/pay/PayInstallmentUseCase
   Notas:
   - El caso de uso hace read -> mutación de dominio -> save.
   - Se puede implementar con SELECT + UPDATE, idealmente en transacción.
   ============================================================================ */

BEGIN;

-- 1) Busca cuota por id.
-- Params:
--   $1 = installment_id
SELECT id, debt_id, installment_no, due_date, paid_at, amount
FROM installments
WHERE id = $1
FOR UPDATE;

-- 2) Marca pago (la validación "ya pagada" también puede protegerse en SQL).
-- Params:
--   $1 = paid_at
--   $2 = installment_id
UPDATE installments
SET paid_at = $1
WHERE id = $2
  AND paid_at IS NULL;

-- Opcional (si luego quieres mantener debts.settled sincronizado):
-- Params:
--   $1 = debt_id de la cuota pagada
-- UPDATE debts d
-- SET settled = NOT EXISTS (
--   SELECT 1
--   FROM installments i
--   WHERE i.debt_id = d.id
--     AND i.paid_at IS NULL
-- )
-- WHERE d.id = $1;

COMMIT;


/* ============================================================================
   SALARY / CreateSalaryUseCase
   Fuente: application/usecases/salary/create/CreateSalaryUseCase
   Notas:
   - Siempre desactiva salario actual y luego inserta uno nuevo activo.
   - Recomendado en una sola transacción.
   ============================================================================ */

BEGIN;

-- 1) Desactiva salario(s) actual(es) activo(s).
UPDATE salaries
SET active = FALSE
WHERE active = TRUE;

-- 2) Inserta nuevo salario activo.
-- Params:
--   $1 = id
--   $2 = amount
--   $3 = created_at
INSERT INTO salaries (id, amount, created_at, active)
VALUES ($1, $2, $3, TRUE);

COMMIT;


/* ============================================================================
   RECURRING EXPENSE / CreateRecurringExpenseUseCase
   Fuente: application/usecases/recurringexpense/create/CreateRecurringExpenseUseCase
   ============================================================================ */

-- Params:
--   $1 = id
--   $2 = description
--   $3 = amount
--   $4 = type ('FIXED' | 'OPTIONAL')
INSERT INTO recurring_expenses (id, description, amount, type)
VALUES ($1, $2, $3, $4);


/* ============================================================================
   RECURRING EXPENSE / ListRecurringExpensesUseCase
   Fuente: application/usecases/recurringexpense/list/ListRecurringExpensesUseCase
   ============================================================================ */

-- Params:
--   $1 = type
SELECT id, description, amount, type
FROM recurring_expenses
WHERE type = $1
ORDER BY description ASC, id ASC;


/* ============================================================================
   RECURRING EXPENSE / UpdateFixedExpenseUseCase
   Fuente: application/usecases/recurringexpense/update/UpdateFixedExpenseUseCase
   Notas:
   - El caso usa findById + update.
   - El nombre "fixed" está en el use case, pero hoy la búsqueda es solo por id.
   ============================================================================ */

BEGIN;

-- 1) Busca por id.
-- Params:
--   $1 = recurring_expense_id
SELECT id, description, amount, type
FROM recurring_expenses
WHERE id = $1
FOR UPDATE;

-- 2) Actualiza descripción/monto.
-- Params:
--   $1 = new_description
--   $2 = new_amount
--   $3 = recurring_expense_id
UPDATE recurring_expenses
SET description = $1,
    amount = $2
WHERE id = $3;

COMMIT;

-- Variante estricta (si quieres asegurar que sea FIXED en SQL):
-- UPDATE recurring_expenses
-- SET description = $1, amount = $2
-- WHERE id = $3
--   AND type = 'FIXED';


/* ============================================================================
   RECURRING EXPENSE / GetRecurringExpenseTotalUseCase
   Fuente: application/usecases/recurringexpense/total/GetRecurringExpenseTotalUseCase
   ============================================================================ */

-- Params:
--   $1 = type
SELECT COALESCE(SUM(amount), 0) AS total
FROM recurring_expenses
WHERE type = $1;


/* ============================================================================
   CONSULTAS DE REPOSITORIO (no necesariamente usadas hoy por un use case directo)
   ============================================================================ */

-- DebtRepository.findByDebtorId
-- Params:
--   $1 = debtor_id
SELECT id, description, total_amount, debtor_id, created_at, settled
FROM debts
WHERE debtor_id = $1
ORDER BY created_at DESC, id ASC;

-- DebtRepository.calculateTotalDebtForDebtor
-- Params:
--   $1 = debtor_id
SELECT COALESCE(SUM(total_amount), 0) AS total_debt
FROM debts
WHERE debtor_id = $1;
