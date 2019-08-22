package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MonthlyPdfInvoiceGenerator {

    public List<Table> generateTables(MonthlyEntity monthlyEntity, List<InvoiceEntity> invoices) {
        List<Table> tables = new ArrayList<>();
//        tables.add(invoiceDetailsTable(monthlyEntity));
//        tables.add(costsTable(invoices));
//        tables.add(footerTable(monthlyEntity.getSkonto(), invoices));
        return tables;
    }

//    private Table invoiceDetailsTable(MonthlyEntity monthlyEntity) {
//        log.info("creating invoice details table");
//        Table table = new Table(2);
//        table.setWidthPercentage(45);
//        table.setHorizontalAlignment(ALIGN_RIGHT);
//
//        table.addCell(cell("Monatsaufstellung", 5));
//        table.addCell(cell(monthlyEntity.getDescription(), 9));
//        table.addCell(cell("Rechnungsdatum", 6));
//        table.addCell(cell(monthlyEntity.getDate().format(ofPattern("dd.MM.yyyy")), 10));
//
//        return table;
//    }
//
//    private Table costsTable(List<InvoiceEntity> invoices) {
//        log.info("creating costs table");
//        Table table = new Table(new float[]{1, 1, 3, 1});
//        table.setWidthPercentage(100);
//        table.setHorizontalAlignment(ALIGN_CENTER);
//        table.setSpacingBefore(20);
//        costsHeaderRow(table);
//
//        for (InvoiceEntity invoice : invoices) {
//            table.addCell(cell(valueOf(invoice.getId())));
//            table.addCell(cell(invoice.getDate().format(ofPattern("dd.MM.yyyy"))));
//            table.addCell(cell(invoice.getPatient()));
//            table.addCell(cellRight(calculateInvoice(invoice).getBrutto().toPlainString()));
//        }
//        return table;
//    }
//
//
//    private void costsHeaderRow(Table table) {
//        table.setHeaderRows(1);
//        table.addCell(headerCell("Rechnung"));
//        table.addCell(headerCell("Datum"));
//        table.addCell(headerCell("Patient"));
//        table.addCell(headerCellRight("Betrag"));
//    }
//
//    private Table footerTable(int skonto, List<InvoiceEntity> invoices) {
//        log.info("creating footer table");
//        Table table = new Table(7);
//        table.setTotalWidth(A4.getWidth() - 100);
//        table.setLockedWidth(true);
//        table.setHorizontalAlignment(ALIGN_CENTER);
//
//        table.addCell(fineCellCentered("Die genannten Beträge sind Bruttobeträge. Die hierin enthaltene Umsatzsteuer ist in den einzelnen Rechnungen ausgewiesen.\nZahlbar innerhalb von zehn (10) Tagen abzüglich Skonto.", 4));
//        Cell blankCell = cell(" ");
//        blankCell.setColspan(3);
//        table.addCell(blankCell);
//        MonthlyInvoiceSum monthlyInvoiceSum = calculateMonthlyInvoiceSum(invoices, skonto);
//
//        addFooterRow(table, "Zwischensumme", monthlyInvoiceSum.getSubtotal(), DEFAULT_FONT, TOP, NO_BORDER);
//        addFooterRow(table, "Ohne Material", monthlyInvoiceSum.getEfforts(), DEFAULT_FONT, NO_BORDER, TOP);
//        addFooterRow(table, format("Skonto (%d%%)", skonto), monthlyInvoiceSum.getSkonto(), DEFAULT_FONT, NO_BORDER, BOTTOM);
//        addFooterRow(table, "Gesamtbetrag", monthlyInvoiceSum.getTotal(), BOLD_FONT, BOTTOM, NO_BORDER);
//
//        table.addCell(fineCellCentered("Umsatzsteuer-Identifikationsnummer DE239653548", table.getNumberOfColumns()));
//
//        return table;
//    }

}
