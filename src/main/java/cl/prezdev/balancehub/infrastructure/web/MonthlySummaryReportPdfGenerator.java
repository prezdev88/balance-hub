package cl.prezdev.balancehub.infrastructure.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import cl.prezdev.balancehub.application.usecases.report.monthly.GetMonthlySummaryReportResult;
import cl.prezdev.balancehub.application.usecases.report.monthly.MonthlySummaryInstallmentItem;
import cl.prezdev.balancehub.domain.enums.SalarySnapshotStatus;

final class MonthlySummaryReportPdfGenerator {

    private static final float PAGE_MARGIN = 48f;
    private static final float LINE_GAP = 16f;
    private static final float TABLE_ROW_GAP = 14f;
    private static final Locale LOCALE_CL = Locale.forLanguageTag("es-CL");
    private static final String[] MONTH_ABBREVIATIONS = { "ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic" };

    private MonthlySummaryReportPdfGenerator() {}

    static byte[] generate(GetMonthlySummaryReportResult report) {
        try (var document = new PDDocument(); var output = new ByteArrayOutputStream()) {
            var page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (var content = new PDPageContentStream(document, page)) {
                var titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                var bodyFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                var smallBoldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                float y = page.getMediaBox().getHeight() - PAGE_MARGIN;
                float left = PAGE_MARGIN;
                float right = page.getMediaBox().getWidth() - PAGE_MARGIN;
                float contentWidth = right - left;

                y = writeLine(content, titleFont, 18, left, y, "Balance Hub - Resumen mensual");
                y = writeLine(content, bodyFont, 12, left, y, report.debtorName());
                y = writeLine(content, bodyFont, 11, left, y, report.debtorEmail());
                y = writeLine(content, bodyFont, 11, left, y, "Periodo: " + monthYearLabel(report.month(), report.year()));

                drawHorizontalLine(content, left, right, y + 4);
                y -= 10;

                y = writeLine(content, smallBoldFont, 12, left, y, "Resumen financiero");
                y = writeLine(content, bodyFont, 11, left, y, "Estado sueldo: " + salaryStatusLabel(report.salaryStatus()));
                y = writeLine(content, bodyFont, 11, left, y, "Monto libre mensual: " + formatCurrency(report.monthlyFreeAmount()));
                y = writeLine(content, bodyFont, 11, left, y, "Mitad de monto libre: " + formatCurrency(report.halfFreeAmount()));
                y = writeLine(content, bodyFont, 11, left, y, "Total cuotas del mes: " + formatCurrency(report.totalInstallmentsAmount()));
                y = writeLine(content, bodyFont, 11, left, y, "Disponible post-cuotas (columna sueldo): " + formatCurrency(report.salaryColumnAmount()));
                y = writeLine(content, bodyFont, 11, left, y, "Fecha pago sueldo: " + formatInstant(report.salaryPaidAt()));

                y -= 6;
                drawHorizontalLine(content, left, right, y + 4);
                y -= 10;

                y = writeLine(content, smallBoldFont, 12, left, y, "Detalle de cuotas");
                y = drawInstallmentsTable(content, bodyFont, smallBoldFont, left, y, contentWidth, report.installments());
            }

            document.save(output);
            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to generate monthly summary PDF", exception);
        }
    }

    private static float writeLine(
        PDPageContentStream content,
        PDType1Font font,
        int size,
        float x,
        float y,
        String text
    ) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
        return y - LINE_GAP;
    }

    private static float drawInstallmentsTable(
        PDPageContentStream content,
        PDType1Font bodyFont,
        PDType1Font boldFont,
        float left,
        float y,
        float width,
        List<MonthlySummaryInstallmentItem> installments
    ) throws IOException {
        if (installments.isEmpty()) {
            return writeLine(content, bodyFont, 11, left, y, "No hay cuotas para este período.");
        }

        float colVence = left;
        float colMonto = left + 78;
        float colEstado = left + 170;
        float colCuota = left + 245;
        float colDeuda = left + 300;

        y = writeLine(content, boldFont, 10, colVence, y, "Vence");
        content.beginText();
        content.setFont(boldFont, 10);
        content.newLineAtOffset(colMonto, y + LINE_GAP);
        content.showText("Monto");
        content.endText();
        content.beginText();
        content.setFont(boldFont, 10);
        content.newLineAtOffset(colEstado, y + LINE_GAP);
        content.showText("Estado");
        content.endText();
        content.beginText();
        content.setFont(boldFont, 10);
        content.newLineAtOffset(colCuota, y + LINE_GAP);
        content.showText("Cuota");
        content.endText();
        content.beginText();
        content.setFont(boldFont, 10);
        content.newLineAtOffset(colDeuda, y + LINE_GAP);
        content.showText("Deuda");
        content.endText();

        drawHorizontalLine(content, left, left + width, y + 2);
        y -= 6;

        for (var installment : installments) {
            if (y < PAGE_MARGIN + 24) {
                y = writeLine(content, bodyFont, 9, left, y, "... (más cuotas omitidas por espacio)");
                break;
            }

            drawCell(content, bodyFont, 10, colVence, y, formatDate(installment.dueDate().toString()));
            drawCell(content, bodyFont, 10, colMonto, y, formatCurrency(installment.amount()));
            drawCell(content, bodyFont, 10, colEstado, y, installment.paid() ? "Pagada" : "Pendiente");
            drawCell(
                content,
                bodyFont,
                10,
                colCuota,
                y,
                installment.installmentNumber() + "/" + installment.totalInstallments()
            );

            String debtText = installment.debtDescription();
            float debtWidth = 240f;
            var lines = wrapText(bodyFont, 10, debtText, debtWidth);
            drawCell(content, bodyFont, 10, colDeuda, y, lines.get(0));
            if (lines.size() > 1 && y - TABLE_ROW_GAP >= PAGE_MARGIN + 18) {
                drawCell(content, bodyFont, 10, colDeuda, y - TABLE_ROW_GAP, lines.get(1));
                y -= TABLE_ROW_GAP;
            }

            y -= TABLE_ROW_GAP;
            drawHorizontalLine(content, left, left + width, y + 4);
            y -= 6;
        }

        return y;
    }

    private static void drawCell(PDPageContentStream content, PDType1Font font, int size, float x, float y, String text) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }

    private static List<String> wrapText(PDType1Font font, int size, String text, float maxWidth) throws IOException {
        var words = text.split("\\s+");
        var lines = new java.util.ArrayList<String>();
        var current = new StringBuilder();

        for (String word : words) {
            String candidate = current.isEmpty() ? word : current + " " + word;
            float width = font.getStringWidth(candidate) / 1000f * size;
            if (width <= maxWidth) {
                current.setLength(0);
                current.append(candidate);
                continue;
            }
            if (!current.isEmpty()) {
                lines.add(current.toString());
            }
            current.setLength(0);
            current.append(word);
        }
        if (!current.isEmpty()) {
            lines.add(current.toString());
        }
        return lines.isEmpty() ? List.of(text) : lines;
    }

    private static void drawHorizontalLine(PDPageContentStream content, float fromX, float toX, float y) throws IOException {
        content.moveTo(fromX, y);
        content.lineTo(toX, y);
        content.stroke();
    }

    private static String salaryStatusLabel(SalarySnapshotStatus status) {
        if (status == null) return "PREVIEW (no guardado)";
        return status == SalarySnapshotStatus.PAID ? "Pagado" : "Pendiente";
    }

    private static String monthYearLabel(int month, int year) {
        String monthName = month >= 1 && month <= 12 ? MONTH_ABBREVIATIONS[month - 1] : String.valueOf(month);
        return monthName + "-" + year;
    }

    private static String formatDate(String isoDate) {
        String[] parts = isoDate.split("-");
        if (parts.length != 3) return isoDate;
        int month = Integer.parseInt(parts[1]);
        String monthName = month >= 1 && month <= 12 ? MONTH_ABBREVIATIONS[month - 1] : parts[1];
        return parts[2] + "-" + monthName + "-" + parts[0];
    }

    private static String formatInstant(Instant instant) {
        if (instant == null) return "-";
        var date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return formatDate(date.toString());
    }

    private static String formatCurrency(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(LOCALE_CL);
        formatter.setMaximumFractionDigits(0);
        formatter.setMinimumFractionDigits(0);
        return formatter.format(amount);
    }
}
