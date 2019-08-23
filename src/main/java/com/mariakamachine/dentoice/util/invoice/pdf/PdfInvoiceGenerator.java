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
import static com.itextpdf.layout.property.UnitValue.createPercentArray;
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
        tables.add(new Table(1).useAllAvailableWidth().addCell(cell(" ").setHeight(5)));
        tables.add(costsTable(invoice.getCosts(), invoiceSum));
        tables.add(new Table(1).useAllAvailableWidth().addCell(cell(" ").setHeight(20)));
        tables.add(new Table(1).useAllAvailableWidth().addCell(fineCell("Hinweis gemäß § 14 Absatz 4 Satz 1 Nr. 7 Umsatzsteuergesetz:\nZahlbar entsprechend Konditionenvereinbarung vom 30.7.2004").setBorderBottom(new SolidBorder(1))));
        tables.add(footerTable(invoice, invoiceSum));
        tables.add(new Table(1).useAllAvailableWidth().addCell(fineCell("Umsatzsteuer-Identifikationsnummer DE239653548").setBorderTop(new SolidBorder(1))));
        return tables;
    }

    private Table invoiceDetailsTable(InvoiceEntity invoice) {
        log.info("creating invoice details table");
        return new Table(2)
                .setWidth(createPercentValue(50))
                .setHorizontalAlignment(RIGHT)

                .addCell(cell("Rechnungsnummer:").setBorderTop(defaultBorder()).setBorderLeft(defaultBorder()))
                .addCell(cell(valueOf(invoice.getId())).setBorderTop(defaultBorder()).setBorderRight(defaultBorder()))
                .addCell(cell("Rechnungsdatum:").setBorderLeft(defaultBorder()))
                .addCell(cell(invoice.getDate().format(ofPattern("dd.MM.yyyy"))).setBorderRight(defaultBorder()).setBorderRight(defaultBorder()))
                .addCell(cell("XML-Nummer:").setBorderLeft(defaultBorder()))
                .addCell(cell(invoice.getXmlNumber()).setBorderRight(defaultBorder()))
                .addCell(cell("Patient:").setBorderLeft(defaultBorder()))
                .addCell(cell(invoice.getPatient()).setBorderRight(defaultBorder()))
                .addCell(cell("Art der Arbeit").setBorderLeft(defaultBorder()))
                .addCell(cell(invoice.getDescription()).setBorderRight(defaultBorder()))
                .addCell(cell("Kassenart:").setBorderLeft(defaultBorder()))
                .addCell(cell(invoice.getInsuranceType().toString()).setBorderRight(defaultBorder()))
                .addCell(cell("Zahnfarbe:").setBorderBottom(defaultBorder()).setBorderLeft(defaultBorder()))
                .addCell(cell(invoice.getColor()).setBorderBottom(defaultBorder()).setBorderRight(defaultBorder()));
    }

    private Border defaultBorder() {
        return new DashedBorder(makeColor(new Gray()), 1);
    }

    private Table costsTable(CostWrapperEntity costs, InvoiceSum invoiceSum) {
        log.info("creating costs table");
        Table table = new Table(createPercentArray(20))
                .useAllAvailableWidth()
                .setHorizontalAlignment(CENTER)

                .addHeaderCell(headerCell("Position", 2))
                .addHeaderCell(headerCell("Zahntechnische Leistung", 11))
                .addHeaderCell(headerCell("Menge", 2))
                .addHeaderCell(headerCellRight("Einzelpreis", 2))
                .addHeaderCell(headerCellRight("Gesamtpreis", 3));

        for (EffortJsonb effort : costs.getEfforts()) {
            table.addCell(cell(effort.getPosition(), 2));
            table.addCell(cell(effort.getName(), 11));
            table.addCell(cell(valueOf(effort.getQuantity()), 2));
            table.addCell(cellRight(valueOf(effort.getPricePerUnit()), 2));
            table.addCell(cellRight(calculateProduct(effort.getQuantity(), effort.getPricePerUnit()).toPlainString(), 3));
        }

        for (MaterialJsonb material : costs.getMaterials()) {
            table.addCell(cell(material.getPosition(), 2));
            String description = material.getName();
            if (isNotBlank(material.getNotes())) {
                description += "\n ";
                description += material.getNotes();
            }
            table.addCell(cell(description, 11));
            table.addCell(cell(valueOf(material.getQuantity()), 2));
            table.addCell(cellRight(BigDecimal.valueOf(material.getPricePerUnit()).toPlainString(), 2));
            table.addCell(cellRight(calculateProduct(material.getQuantity(), material.getPricePerUnit()).toPlainString(), 3));
        }

        addEmptyRow(table);

        table.addCell(cell("", 2));
        Cell metalsSumRow = cell(">> Berechnetes Edelmetall", 15);
        table.addCell(metalsSumRow);
        table.addCell(cellRight(invoiceSum.getMetal().toPlainString(), 3));

        addEmptyRow(table);

        table.addCell(cell(""));
        table.addCell(fineCell("Diese Sonderanfertigung wurde unter Einhaltung der grundlegenden Anforderungen des Anhang I der Richlinie 93/42/EWG erstellt. Sie ist ausschliesslich für den oben genannten Patienten bestimmt.", 16));

        return table;
    }

    private Table footerTable(InvoiceEntity invoice, InvoiceSum invoiceSum) {
        log.info("creating footer table");
        return new Table(2)
                .setWidth(A4.getWidth() / 3)
                .setHorizontalAlignment(RIGHT)

                .addCell(cell("Material"))
                .addCell(cellRight(invoiceSum.getMaterials().toPlainString().concat(" €")))

                .addCell(cell("Leistung"))
                .addCell(cellRight(invoiceSum.getEfforts().toPlainString().concat(" €")))

                .addCell(cell("Netto").setBorderTop(new SolidBorder(1)))
                .addCell(cellRight(invoiceSum.getNetto().toPlainString().concat(" €")).setBorderTop(new SolidBorder(1)))

                .addCell(cell(format("Mehrwertsteuer (%s%%)", invoice.getMwst())))
                .addCell(cellRight(invoiceSum.getMwst().toPlainString().concat(" €")))

                .addCell(cell("Gesamtbetrag").setBorderTop(new SolidBorder(1)).setBold())
                .addCell(cellRight(invoiceSum.getBrutto().toPlainString().concat(" €")).setBorderTop(new SolidBorder(1)).setBold());
    }

}
