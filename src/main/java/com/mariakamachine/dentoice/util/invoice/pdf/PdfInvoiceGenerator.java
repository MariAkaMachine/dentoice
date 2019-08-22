package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import com.mariakamachine.dentoice.util.invoice.InvoiceSum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.itextpdf.layout.property.HorizontalAlignment.CENTER;
import static com.itextpdf.layout.property.HorizontalAlignment.RIGHT;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateInvoice;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateProduct;
import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.*;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
class PdfInvoiceGenerator {

    public List<Table> generateTables(InvoiceEntity invoice) {
        List<Table> tables = new ArrayList<>();
        InvoiceSum invoiceSum = calculateInvoice(invoice);
        tables.add(invoiceDetailsTable(invoice));
        tables.add(costsTable(invoice.getCosts(), invoiceSum));
        tables.add(footerTable(invoice, invoiceSum));
        return tables;
    }

    private Table invoiceDetailsTable(InvoiceEntity invoice) {
        log.info("creating invoice details table");
        Table table = new Table(2);
//        table.setWidthPercentage(45);
        table.setHorizontalAlignment(RIGHT);

        table.addCell(cell("Rechnungsnummer")).setBorderTop(new SolidBorder(1)).setBorderLeft(new SolidBorder(1));
        table.addCell(cell(valueOf(invoice.getId())).setBorderTop(new SolidBorder(1)).setBorderRight(new SolidBorder(1)));
        table.addCell(cell("Rechnungsdatum").setBorderLeft(new SolidBorder(1)));
        table.addCell(cell(invoice.getDate().format(ofPattern("dd.MM.yyyy"))).setBorderRight(new SolidBorder(1)));
        table.addCell(cell("XML-Nummer")).setBorderLeft(new SolidBorder(1));
        table.addCell(cell(invoice.getXmlNumber()).setBorderRight(new SolidBorder(1)));
        table.addCell(cell("Art der Arbeit").setBorderLeft(new SolidBorder(1)));
        table.addCell(cell(invoice.getDescription()).setBorderRight(new SolidBorder(1)));
        table.addCell(cell("Patient").setBorderLeft(new SolidBorder(1)));
        table.addCell(cell(invoice.getPatient()).setBorderRight(new SolidBorder(1)));
        table.addCell(cell("Kassenart").setBorderLeft(new SolidBorder(1)));
        table.addCell(cell(invoice.getInsuranceType().toString()).setBorderRight(new SolidBorder(1)));
        table.addCell(cell("Zahnfarbe").setBorderBottom(new SolidBorder(1)).setBorderLeft(new SolidBorder(1)));
        table.addCell(cell(isBlank(invoice.getColor()) ? " " : invoice.getColor()).setBorderBottom(new SolidBorder(1)).setBorderRight(new SolidBorder(1)));
        return table;
    }

    private Table costsTable(CostWrapperEntity costs, InvoiceSum invoiceSum) {
        log.info("creating costs table");
        Table table = new Table(new float[]{2, 9, 2, 3, 3});
//        table.setWidthPercentage(100);
        table.setHorizontalAlignment(CENTER);
//        table.setSpacingBefore(20);
        costsHeaderRow(table);

        for (EffortJsonb effort : costs.getEfforts()) {
            table.addCell(cell(effort.getPosition()));
            table.addCell(cell(effort.getName()));
            table.addCell(cell(valueOf(effort.getQuantity())));
            table.addCell(cellRight(valueOf(effort.getPricePerUnit())));
            table.addCell(cellRight(calculateProduct(effort.getQuantity(), effort.getPricePerUnit()).toPlainString()));
        }

        for (MaterialJsonb material : costs.getMaterials()) {
            table.addCell(cell(material.getPosition()));
            String description = material.getName();
            if (isNotBlank(material.getNotes())) {
                description += "\n ";
                description += material.getNotes();
            }
            table.addCell(cell(description));
            table.addCell(cell(valueOf(material.getQuantity())));
            table.addCell(cellRight(valueOf(material.getPricePerUnit())));
            table.addCell(cellRight(calculateProduct(material.getQuantity(), material.getPricePerUnit()).toPlainString()));
        }

//        addEmptyRow(table);

        table.addCell(cell(""));
        Cell metalsSumRow = cell("Berechnetes Edelmetall");
//        metalsSumRow.setColspan(3);
        table.addCell(metalsSumRow);
        table.addCell(cellRight(invoiceSum.getMetal().toPlainString()));

//        addEmptyRow(table);

        table.addCell(cell(""));
        table.addCell(fineCell("Diese Sonderanfertigung wurde unter Einhaltung der grundlegenden Anforderungen des Anhang I der Richlinie 93/42/EWG erstellt. Sie ist ausschliesslich für den oben genannten Patienten bestimmt.", 3));
        table.addCell(cell(""));

        return table;
    }

    private void costsHeaderRow(Table table) {
//        table.setHeaderRows(1);
        table.addCell(headerCell("Position"));
        table.addCell(headerCell("Zahntechnische Leistung"));
        table.addCell(headerCell("Menge"));
        table.addCell(headerCellRight("Einzelpreis"));
        table.addCell(headerCellRight("Gesamtpreis"));
    }

    private Table footerTable(InvoiceEntity invoice, InvoiceSum invoiceSum) {
        log.info("creating footer table");
        Table table = new Table(7);
//        table.setTotalWidth(A4.getWidth() - 100);
//        table.setLockedWidth(true);
        table.setHorizontalAlignment(CENTER);

        table.addCell(fineCell("Hinweis gemäß § 14 Absatz 4 Satz 1 Nr. 7 Umsatzsteuergesetz:", table.getNumberOfColumns()));
        table.addCell(fineCell("Zahlbar entsprechend Konditionenvereinbarung vom 30.7.2004", table.getNumberOfColumns()));

//        addFooterRow(table, "Material", invoiceSum.getMaterials(), null, BORDER_TOP, NO_BORDER);
//        addFooterRow(table, "Leistung", invoiceSum.getEfforts(), null, NO_BORDER, NO_BORDER);
//        addFooterRow(table, "Netto", invoiceSum.getNetto(), null, NO_BORDER, BORDER_TOP);
//        addFooterRow(table, format("Mehrwertsteuer (%s%%)", invoice.getMwst()), invoiceSum.getMwst(), null, NO_BORDER, BORDER_BOTTOM);
//        addFooterRow(table, "Gesamtbetrag", invoiceSum.getBrutto(), null, BORDER_BOTTOM, NO_BORDER);

        table.addCell(fineCell("Umsatzsteuer-Identifikationsnummer DE239653548", table.getNumberOfColumns()));

        return table;
    }

}
