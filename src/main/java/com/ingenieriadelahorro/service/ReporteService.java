package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.model.*;
import com.ingenieriadelahorro.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class ReporteService {

    @Autowired private IngresoRepository ingresoRepository;
    @Autowired private GastoFijoRepository gastoFijoRepository;
    @Autowired private GastoHormigaRepository gastoHormigaRepository;
    @Autowired private DeudaRepository deudaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    private static final NumberFormat COP = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    public byte[] generarPDF(Long usuarioId, Integer mes, Integer anio) throws Exception {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        List<Ingreso> ingresos = ingresoRepository.findByUsuarioIdAndMesAndAnioOrderByFechaDesc(usuarioId, mes, anio);
        List<GastoFijo> gastosFijos = gastoFijoRepository.findByUsuarioIdAndMesAndAnioOrderByFechaPagoDesc(usuarioId, mes, anio);
        List<GastoHormiga> gastosHormiga = gastoHormigaRepository.findByUsuarioIdAndMesAndAnioOrderByFechaDesc(usuarioId, mes, anio);

        BigDecimal totalIngresos = ingresoRepository.sumByUsuarioIdAndMesAndAnio(usuarioId, mes, anio);
        BigDecimal totalFijos = gastoFijoRepository.sumByUsuarioIdAndMesAndAnio(usuarioId, mes, anio);
        BigDecimal totalHormiga = gastoHormigaRepository.sumByUsuarioIdAndMesAndAnio(usuarioId, mes, anio);
        BigDecimal totalGastos = totalFijos.add(totalHormiga);
        BigDecimal ahorro = totalIngresos.subtract(totalGastos);

        Document doc = new Document(PageSize.A4, 40, 40, 60, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();

        // Colores
        BaseColor azul = new BaseColor(27, 79, 114);
        BaseColor verde = new BaseColor(39, 174, 96);
        BaseColor rojo = new BaseColor(231, 76, 60);
        BaseColor gris = new BaseColor(244, 246, 247);

        // Título
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 22, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell headerCell = new PdfPCell(new Phrase("💰 Ingeniería del Ahorro", titleFont));
        headerCell.setBackgroundColor(azul);
        headerCell.setPadding(15);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.addCell(headerCell);
        doc.add(header);
        doc.add(new Paragraph(" "));

        // Subtítulo
        com.itextpdf.text.Font subFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD, azul);
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        doc.add(new Paragraph("Reporte Financiero - " + meses[mes] + " " + anio, subFont));
        doc.add(new Paragraph("Usuario: " + usuario.getNombre(), new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11)));
        doc.add(new Paragraph(" "));

        // Resumen
        PdfPTable resumen = new PdfPTable(2);
        resumen.setWidthPercentage(100);
        resumen.setSpacingBefore(10);
        addResumenRow(resumen, "Total Ingresos", COP.format(totalIngresos), verde);
        addResumenRow(resumen, "Total Gastos Fijos", COP.format(totalFijos), rojo);
        addResumenRow(resumen, "Total Gastos Hormiga", COP.format(totalHormiga), rojo);
        addResumenRow(resumen, "Total Gastos", COP.format(totalGastos), rojo);
        addResumenRow(resumen, "Ahorro del Mes", COP.format(ahorro), ahorro.compareTo(BigDecimal.ZERO) >= 0 ? verde : rojo);
        if (totalIngresos.compareTo(BigDecimal.ZERO) > 0) {
            double pct = ahorro.divide(totalIngresos, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
            addResumenRow(resumen, "% Ahorrado", String.format("%.1f%%", pct), azul);
        }
        doc.add(resumen);
        doc.add(new Paragraph(" "));

        // Tabla ingresos
        if (!ingresos.isEmpty()) {
            doc.add(new Paragraph("INGRESOS", subFont));
            doc.add(new Paragraph(" "));
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{3, 2, 2, 2});
            addTableHeader(tabla, azul, "Nombre", "Categoría", "Valor", "Fecha");
            for (Ingreso i : ingresos) {
                addTableRow(tabla, i.getNombre(), i.getCategoria(), COP.format(i.getValor()), i.getFecha().toString());
            }
            doc.add(tabla);
            doc.add(new Paragraph(" "));
        }

        // Tabla gastos fijos
        if (!gastosFijos.isEmpty()) {
            doc.add(new Paragraph("GASTOS FIJOS", subFont));
            doc.add(new Paragraph(" "));
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{3, 2, 2, 2});
            addTableHeader(tabla, rojo, "Nombre", "Categoría", "Valor", "Estado");
            for (GastoFijo g : gastosFijos) {
                addTableRow(tabla, g.getNombre(), g.getCategoria() != null ? g.getCategoria() : "-", COP.format(g.getValor()), g.getEstado());
            }
            doc.add(tabla);
            doc.add(new Paragraph(" "));
        }

        // Tabla gastos hormiga
        if (!gastosHormiga.isEmpty()) {
            doc.add(new Paragraph("GASTOS HORMIGA", subFont));
            doc.add(new Paragraph(" "));
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{3, 2, 2, 2});
            addTableHeader(tabla, new BaseColor(243, 156, 18), "Nombre", "Categoría", "Valor", "Fecha");
            for (GastoHormiga g : gastosHormiga) {
                addTableRow(tabla, g.getNombre(), g.getCategoria(), COP.format(g.getValor()), g.getFecha().toString());
            }
            doc.add(tabla);
        }

        doc.close();
        return out.toByteArray();
    }

    public byte[] generarExcel(Long usuarioId, Integer mes, Integer anio) throws Exception {
        List<Ingreso> ingresos = ingresoRepository.findByUsuarioIdAndMesAndAnioOrderByFechaDesc(usuarioId, mes, anio);
        List<GastoFijo> gastosFijos = gastoFijoRepository.findByUsuarioIdAndMesAndAnioOrderByFechaPagoDesc(usuarioId, mes, anio);
        List<GastoHormiga> gastosHormiga = gastoHormigaRepository.findByUsuarioIdAndMesAndAnioOrderByFechaDesc(usuarioId, mes, anio);

        XSSFWorkbook workbook = new XSSFWorkbook();

        // Hoja Ingresos
        Sheet sheetIngresos = workbook.createSheet("Ingresos");
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheetIngresos.createRow(0);
        String[] ingresosHeaders = {"ID", "Nombre", "Categoría", "Valor", "Fecha", "Método Pago", "Descripción"};
        for (int i = 0; i < ingresosHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(ingresosHeaders[i]);
            cell.setCellStyle(headerStyle);
        }
        int rowNum = 1;
        for (Ingreso ing : ingresos) {
            Row row = sheetIngresos.createRow(rowNum++);
            row.createCell(0).setCellValue(ing.getId());
            row.createCell(1).setCellValue(ing.getNombre());
            row.createCell(2).setCellValue(ing.getCategoria());
            row.createCell(3).setCellValue(ing.getValor().doubleValue());
            row.createCell(4).setCellValue(ing.getFecha().toString());
            row.createCell(5).setCellValue(ing.getMetodoPago() != null ? ing.getMetodoPago() : "");
            row.createCell(6).setCellValue(ing.getDescripcion() != null ? ing.getDescripcion() : "");
        }

        // Hoja Gastos Fijos
        Sheet sheetFijos = workbook.createSheet("Gastos Fijos");
        Row hFijos = sheetFijos.createRow(0);
        String[] fijosHeaders = {"ID", "Nombre", "Categoría", "Valor", "Fecha Pago", "Estado", "Prioridad"};
        for (int i = 0; i < fijosHeaders.length; i++) {
            Cell cell = hFijos.createCell(i);
            cell.setCellValue(fijosHeaders[i]);
            cell.setCellStyle(headerStyle);
        }
        rowNum = 1;
        for (GastoFijo g : gastosFijos) {
            Row row = sheetFijos.createRow(rowNum++);
            row.createCell(0).setCellValue(g.getId());
            row.createCell(1).setCellValue(g.getNombre());
            row.createCell(2).setCellValue(g.getCategoria() != null ? g.getCategoria() : "");
            row.createCell(3).setCellValue(g.getValor().doubleValue());
            row.createCell(4).setCellValue(g.getFechaPago() != null ? g.getFechaPago().toString() : "");
            row.createCell(5).setCellValue(g.getEstado());
            row.createCell(6).setCellValue(g.getPrioridad());
        }

        // Hoja Gastos Hormiga
        Sheet sheetHormiga = workbook.createSheet("Gastos Hormiga");
        Row hHormiga = sheetHormiga.createRow(0);
        String[] hormigaHeaders = {"ID", "Nombre", "Categoría", "Valor", "Fecha", "Descripción"};
        for (int i = 0; i < hormigaHeaders.length; i++) {
            Cell cell = hHormiga.createCell(i);
            cell.setCellValue(hormigaHeaders[i]);
            cell.setCellStyle(headerStyle);
        }
        rowNum = 1;
        for (GastoHormiga g : gastosHormiga) {
            Row row = sheetHormiga.createRow(rowNum++);
            row.createCell(0).setCellValue(g.getId());
            row.createCell(1).setCellValue(g.getNombre());
            row.createCell(2).setCellValue(g.getCategoria());
            row.createCell(3).setCellValue(g.getValor().doubleValue());
            row.createCell(4).setCellValue(g.getFecha().toString());
            row.createCell(5).setCellValue(g.getDescripcion() != null ? g.getDescripcion() : "");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    private void addResumenRow(PdfPTable table, String label, String value, BaseColor color) {
        com.itextpdf.text.Font labelFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font valueFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD, color);
        PdfPCell c1 = new PdfPCell(new Phrase(label, labelFont));
        PdfPCell c2 = new PdfPCell(new Phrase(value, valueFont));
        c1.setPadding(8); c2.setPadding(8);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1); table.addCell(c2);
    }

    private void addTableHeader(PdfPTable table, BaseColor color, String... headers) {
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, font));
            cell.setBackgroundColor(color);
            cell.setPadding(6);
            table.addCell(cell);
        }
    }

    private void addTableRow(PdfPTable table, String... values) {
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9);
        for (String v : values) {
            PdfPCell cell = new PdfPCell(new Phrase(v != null ? v : "", font));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
