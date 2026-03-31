-- Listar cuotas según el nombre del deudor, el mes y el año de vencimiento
SELECT 
    i.id AS cuota_id,
    dr.name AS deudor,
    d.description AS concepto_deuda,
    i.installment_no AS cuota_nro,
    i.due_date AS fecha_vencimiento,
    i.amount AS monto,
    CASE 
        WHEN i.paid_at IS NOT NULL THEN 'PAGADO' 
        ELSE 'PENDIENTE' 
    END AS estado
FROM installments i
JOIN debts d ON i.debt_id = d.id
JOIN debtors dr ON d.debtor_id = dr.id
WHERE 
    dr.name ILIKE '%Fabiola%'
    AND EXTRACT(MONTH FROM i.due_date) = 4
    AND EXTRACT(YEAR FROM i.due_date) = 2026
ORDER BY i.due_date ASC;

-- Cancelar el pago de una cuota
UPDATE installments 
SET paid_at = NULL 
WHERE id = 'installment_id_a_cancelar';

-- Pagar una cuota
UPDATE installments 
SET paid_at = NOW() 
WHERE id = 'installment_id_a_pagar';