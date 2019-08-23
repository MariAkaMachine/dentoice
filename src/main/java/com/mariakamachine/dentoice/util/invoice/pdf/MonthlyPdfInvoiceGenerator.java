package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import com.mariakamachine.dentoice.util.invoice.MonthlyInvoiceSum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.itextpdf.kernel.colors.Color.makeColor;
import static com.itextpdf.kernel.geom.PageSize.A4;
import static com.itextpdf.layout.property.HorizontalAlignment.CENTER;
import static com.itextpdf.layout.property.HorizontalAlignment.RIGHT;
import static com.itextpdf.layout.property.UnitValue.createPercentValue;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateInvoice;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateMonthlyInvoiceSum;
import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.*;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
public class MonthlyPdfInvoiceGenerator {

    public List<Table> generateTables(MonthlyEntity monthlyEntity, List<InvoiceEntity> invoices) {
        List<Table> tables = new ArrayList<>();
        tables.add(invoiceDetailsTable(monthlyEntity));
        tables.add(new Table(1).useAllAvailableWidth().addCell(cell(" ").setHeight(91)));
        tables.add(costsTable(invoices));
        tables.add(new Table(1).useAllAvailableWidth().addCell(cell(" ").setHeight(20)));
        tables.add(new Table(1).useAllAvailableWidth().addCell(fineCell("Hinweis gemäß § 14 Absatz 4 Satz 1 Nr. 7 Umsatzsteuergesetz:\nZahlbar entsprechend Konditionenvereinbarung vom 30.7.2004").setBorderBottom(new SolidBorder(1))));
        tables.add(footerTable(monthlyEntity.getSkonto(), invoices));
        tables.add(new Table(1).useAllAvailableWidth().addCell(fineCell("Umsatzsteuer-Identifikationsnummer DE239653548").setBorderTop(new SolidBorder(1))));
        return tables;
    }

    private Table invoiceDetailsTable(MonthlyEntity monthlyEntity) {
        log.info("creating invoice details table");
        return new Table(2)
                .setWidth(createPercentValue(50))
                .setHorizontalAlignment(RIGHT)

                .addCell(cell("Monatsaufstellung").setBorderLeft(defaultBorder()).setBorderTop(defaultBorder()))
                .addCell(cell(monthlyEntity.getDescription()).setBorderTop(defaultBorder()).setBorderRight(defaultBorder()))
                .addCell(cell("Rechnungsdatum").setBorderLeft(defaultBorder()).setBorderBottom(defaultBorder()))
                .addCell(cell(monthlyEntity.getDate().format(ofPattern("dd.MM.yyyy"))).setBorderBottom(defaultBorder()).setBorderRight(defaultBorder()));
    }

    private Border defaultBorder() {
        return new DashedBorder(makeColor(new PdfDeviceCs.Gray()), 1);
    }

    private Table costsTable(List<InvoiceEntity> invoices) {
        log.info("creating costs table");
        Table table = new Table(new float[]{1, 1, 3, 1})
                .useAllAvailableWidth()
                .setHorizontalAlignment(CENTER)

                .addHeaderCell(headerCell("Rechnung"))
                .addHeaderCell(headerCell("Datum"))
                .addHeaderCell(headerCell("Patient"))
                .addHeaderCell(headerCellRight("Betrag"));

        for (InvoiceEntity invoice : invoices) {
            table.addCell(cell(valueOf(invoice.getId())));
            table.addCell(cell(invoice.getDate().format(ofPattern("dd.MM.yyyy"))));
            table.addCell(cell(invoice.getPatient()));
            table.addCell(cellRight(calculateInvoice(invoice).getBrutto().toPlainString()));
        }
        return table;
    }

    private Table footerTable(int skonto, List<InvoiceEntity> invoices) {
        log.info("creating footer table");
        MonthlyInvoiceSum monthlyInvoiceSum = calculateMonthlyInvoiceSum(invoices, skonto);
        return new Table(2)
                .setWidth(A4.getWidth() / 3)
                .setHorizontalAlignment(RIGHT)

                .addCell(cell("Zwischensumme"))
                .addCell(cellRight(monthlyInvoiceSum.getSubtotal().toPlainString().concat(" €")))
                .addCell(cell("Ohne Material").setBorderTop(new SolidBorder(1)))
                .addCell(cellRight(monthlyInvoiceSum.getEfforts().toPlainString().concat(" €")).setBorderTop(new SolidBorder(1)))
                .addCell(cell(format("Skonto (%d%%)", skonto)).setBorderBottom(new SolidBorder(1)))
                .addCell(cellRight(monthlyInvoiceSum.getSkonto().toPlainString().concat(" €")).setBorderBottom(new SolidBorder(1)))
                .addCell(cell("Gesamtbetrag").setBold())
                .addCell(cellRight(monthlyInvoiceSum.getTotal().toPlainString().concat(" €")).setBold());
    }

}
