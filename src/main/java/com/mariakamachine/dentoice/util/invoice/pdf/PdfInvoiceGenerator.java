package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs.Gray;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import com.mariakamachine.dentoice.util.invoice.InvoiceSum;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.itextpdf.kernel.colors.Color.makeColor;
import static com.itextpdf.kernel.geom.PageSize.A4;
import static com.itextpdf.layout.property.HorizontalAlignment.CENTER;
import static com.itextpdf.layout.property.HorizontalAlignment.RIGHT;
import static com.itextpdf.layout.property.UnitValue.createPercentValue;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateInvoice;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateProduct;
import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.*;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
class PdfInvoiceGenerator {

    public List<Table> generateTables(InvoiceEntity invoice) {
        List<Table> tables = new ArrayList<>();
        InvoiceSum invoiceSum = calculateInvoice(invoice);
        tables.add(invoiceDetailsTable(invoice));
        tables.add(new Table(1).useAllAvailableWidth().addCell(fineCellCentered(" ")));
        tables.add(costsTable(invoice.getCosts(), invoiceSum));
        tables.add(new Table(1).useAllAvailableWidth().addCell(fineCell("Hinweis gemäß § 14 Absatz 4 Satz 1 Nr. 7 Umsatzsteuergesetz:\nZahlbar entsprechend Konditionenvereinbarung vom 30.7.2004").setBorderBottom(new SolidBorder(1))));
        tables.add(footerTable(invoice, invoiceSum));
        tables.add(new Table(1).useAllAvailableWidth().addCell(fineCell("Umsatzsteuer-Identifikationsnummer DE239653548").setBorderTop(new SolidBorder(1))));
        return tables;
    }

    private Table invoiceDetailsTable(InvoiceEntity invoice) {
        log.info("creating invoice details table");
        Table table = new Table(2);
        table.setWidth(createPercentValue(50));
        table.setHorizontalAlignment(RIGHT);
        table.addCell(cell("Rechnungsnummer:").setBorderTop(defaultBorder()).setBorderLeft(defaultBorder()));
        table.addCell(cell(valueOf(invoice.getId())).setBorderTop(defaultBorder()).setBorderRight(defaultBorder()));
        table.addCell(cell("Rechnungsdatum:").setBorderLeft(defaultBorder()));
        table.addCell(cell(invoice.getDate().format(ofPattern("dd.MM.yyyy"))).setBorderRight(defaultBorder()).setBorderRight(defaultBorder()));
        table.addCell(cell("XML-Nummer:").setBorderLeft(defaultBorder()));
        table.addCell(cell(invoice.getXmlNumber()).setBorderRight(defaultBorder()));
        table.addCell(cell("Patient:").setBorderLeft(defaultBorder()));
        table.addCell(cell(invoice.getPatient()).setBorderRight(defaultBorder()));
        table.addCell(cell("Art der Arbeit").setBorderLeft(defaultBorder()));
        table.addCell(cell(invoice.getDescription()).setBorderRight(defaultBorder()));
        table.addCell(cell("Kassenart:").setBorderLeft(defaultBorder()));
        table.addCell(cell(invoice.getInsuranceType().toString()).setBorderRight(defaultBorder()));
        table.addCell(cell("Zahnfarbe:").setBorderBottom(defaultBorder()).setBorderLeft(defaultBorder()));
        table.addCell(cell(invoice.getColor()).setBorderBottom(defaultBorder()).setBorderRight(defaultBorder()));
        return table;
    }

    private Border defaultBorder() {
        return new DashedBorder(makeColor(new Gray()), 2);
    }

    private Table costsTable(CostWrapperEntity costs, InvoiceSum invoiceSum) {
        log.info("creating costs table");
        Table table = new Table(20);
        table.useAllAvailableWidth();
        table.setHorizontalAlignment(CENTER);
        costsHeaderRow(table);

        for (EffortJsonb effort : costs.getEfforts()) {
            table.addCell(cell(effort.getPosition()));
            table.addCell(cell(effort.getName(), 13));
            table.addCell(cell(valueOf(effort.getQuantity())));
            table.addCell(cellRight(valueOf(effort.getPricePerUnit())));
            table.addCell(cellRight(calculateProduct(effort.getQuantity(), effort.getPricePerUnit()).toPlainString(), 4));
        }

        for (MaterialJsonb material : costs.getMaterials()) {
            table.addCell(cell(material.getPosition()));
            String description = material.getName();
            if (isNotBlank(material.getNotes())) {
                description += "\n ";
                description += material.getNotes();
            }
            table.addCell(cell(description, 13));
            table.addCell(cell(valueOf(material.getQuantity())));
            table.addCell(cellRight(BigDecimal.valueOf(material.getPricePerUnit()).toPlainString()));
            table.addCell(cellRight(calculateProduct(material.getQuantity(), material.getPricePerUnit()).toPlainString(), 4));
        }

        addEmptyRow(table);

        table.addCell(cell(""));
        Cell metalsSumRow = cell(">>> Berechnetes Edelmetall", 15);
        table.addCell(metalsSumRow);
        table.addCell(cellRight(invoiceSum.getMetal().toPlainString(), 4));

        addEmptyRow(table);

        table.addCell(cell(""));
        table.addCell(fineCell("Diese Sonderanfertigung wurde unter Einhaltung der grundlegenden Anforderungen des Anhang I der Richlinie 93/42/EWG erstellt. Sie ist ausschliesslich für den oben genannten Patienten bestimmt.", 15));

        return table;
    }

    private void costsHeaderRow(Table table) {
        table.addCell(headerCell("Position", 1));
        table.addCell(headerCell("Zahntechnische Leistung", 13));
        table.addCell(headerCell("Menge", 1));
        table.addCell(headerCellRight("Einzelpreis", 1));
        table.addCell(headerCellRight("Gesamtpreis", 4));
    }

    private Table footerTable(InvoiceEntity invoice, InvoiceSum invoiceSum) {
        log.info("creating footer table");
        Table table = new Table(2);
        table.setWidth(A4.getWidth() / 3);
        table.setHorizontalAlignment(RIGHT);

        table.addCell(cell("Material"));
        table.addCell(cellRight(invoiceSum.getMaterials().toPlainString().concat(" €")));

        table.addCell(cell("Leistung"));
        table.addCell(cellRight(invoiceSum.getEfforts().toPlainString().concat(" €")));

        table.addCell(cell("Netto").setBorderTop(new SolidBorder(1)));
        table.addCell(cellRight(invoiceSum.getNetto().toPlainString().concat(" €")).setBorderTop(new SolidBorder(1)));

        table.addCell(cell(format("Mehrwertsteuer (%s%%)", invoice.getMwst())));
        table.addCell(cellRight(invoiceSum.getMwst().toPlainString().concat(" €")));

        table.addCell(cell("Gesamtbetrag").setBorderTop(new SolidBorder(1)).setBold());
        table.addCell(cellRight(invoiceSum.getBrutto().toPlainString().concat(" €")).setBorderTop(new SolidBorder(1)).setBold());
        return table;
    }

}
