package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mariakamachine.dentoice.config.properties.InvoiceProperties;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.enums.InsuranceType;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import com.mariakamachine.dentoice.util.invoice.InvoiceSum;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_RIGHT;
import static com.itextpdf.text.PageSize.A4;
import static com.itextpdf.text.Rectangle.*;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.*;
import static com.mariakamachine.dentoice.util.invoice.pdf.InvoicePdfGenerator.*;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
class PdfInvoice {

    public List<PdfPTable> generateTables(InvoiceProperties invoiceProperties, InvoiceEntity invoice) {
        List<PdfPTable> tables = new ArrayList<>();
        InvoiceSum invoiceSum = calculateInvoice(invoice);
        tables.add(invoiceDetailsTable(invoice));
        tables.add(costsTable(invoice.getCosts(), invoiceSum));
        tables.add(footerTable(invoiceProperties, invoiceSum));
        return tables;
    }

    private PdfPTable invoiceDetailsTable(InvoiceEntity invoice) {
        log.info("creating invoice details table");
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(45);
        table.setHorizontalAlignment(ALIGN_RIGHT);

        /*
         * REMOVE
         */
        invoice.setId(2L);
        invoice.setDate(LocalDate.now());
        invoice.setDescription("24 Tele");
        invoice.setPatient("Dieter MacAdam");
        invoice.setInsuranceType(InsuranceType.PRIVATE);
        /*
         * REMOVE
         */

        table.addCell(cell("Rechnungsnummer", 5));
        table.addCell(cell(valueOf(invoice.getId()), 9));
        table.addCell(cell("Rechnungsdatum", 4));
        table.addCell(cell(invoice.getDate().format(ofPattern("dd.MM.yyyy")), 8));
        table.addCell(cell("Art der Arbeit", 4));
        table.addCell(cell(invoice.getDescription(), 8));
        table.addCell(cell("Patient", 4));
        table.addCell(cell(invoice.getPatient(), 8));
        table.addCell(cell("Kassenart", 4));
        table.addCell(cell(invoice.getInsuranceType().toString(), 8));
        table.addCell(cell("Zahnfarbe", 6));
        table.addCell(cell("gelb", 10));

        table.setSpacingAfter(20);

        return table;
    }

    private PdfPTable costsTable(CostWrapperEntity costs, InvoiceSum invoiceSum) {
        log.info("creating costs table");
        PdfPTable table = new PdfPTable(new float[]{2, 9, 2, 3, 3});
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(ALIGN_CENTER);
        costsHeaderRow(table);

        /*
         * REMOVE
         */
        costs = new CostWrapperEntity();
        EffortJsonb effortb = new EffortJsonb();
        effortb.setPosition("1234");
        effortb.setDescription("Desinfektion");
        effortb.setQuantity(1.00);
        effortb.setPricePerUnit(285.50);
        costs.setEfforts(singletonList(effortb));
        MaterialJsonb material2 = new MaterialJsonb();
        material2.setPosition("7895");
        material2.setDescription("PlatinLloyd 100");
        material2.setNotes("BEGO Legierung CE ja");
        material2.setQuantity(3.60);
        material2.setPricePerUnit(51.25);
        material2.setMetal(true);
        MaterialJsonb material1 = new MaterialJsonb();
        material1.setPosition("7895");
        material1.setDescription("PlatinLloyd 100");
        material1.setQuantity(3.60);
        material1.setPricePerUnit(51.25);
        costs.setMaterials(Arrays.asList(material1, material2));
        for (int i = 0; i < 100; i++) {
            /*
             * REMOVE
             */
            for (EffortJsonb effort : costs.getEfforts()) {
                table.addCell(cell(effort.getPosition()));
                table.addCell(cell(effort.getDescription()));
                table.addCell(cell(valueOf(effort.getQuantity())));
                table.addCell(cellRight(valueOf(effort.getPricePerUnit())));
                table.addCell(cellRight(calculateProduct(effort.getQuantity(), effort.getPricePerUnit()).toPlainString()));
            }
        }

        for (MaterialJsonb material : costs.getMaterials()) {
            table.addCell(cell(material.getPosition()));
            String description = material.getDescription();
            if (isNotBlank(material.getNotes())) {
                description += "\n ";
                description += material.getNotes();
            }
            table.addCell(cell(description));
            table.addCell(cell(valueOf(material.getQuantity())));
            table.addCell(cellRight(valueOf(material.getPricePerUnit())));
            table.addCell(cellRight(calculateProduct(material.getQuantity(), material.getPricePerUnit()).toPlainString()));
        }

        addEmptyRow(table);

        table.addCell(cell(""));
        PdfPCell metalsSumRow = cell("Berechnetes Edelmetall");
        metalsSumRow.setColspan(3);
        table.addCell(metalsSumRow);
        table.addCell(cellRight(invoiceSum.getMetal().toPlainString()));

        addEmptyRow(table);

        table.addCell(cell(""));
        table.addCell(fineCell("Diese Sonderanfertigung wurde unter Einhaltung der grundlegenden Anforderungen des Anhang I der Richlinie 93/42/EWG erstellt. Sie ist ausschliesslich fuer den oben genannten Patienten bestimmt.", 3));
        table.addCell(cell(""));

        return table;
    }

    private void costsHeaderRow(PdfPTable table) {
        table.setHeaderRows(1);
        table.addCell(headerCell("Position"));
        table.addCell(headerCell("Zahntechnische Leistung"));
        table.addCell(headerCell("Menge"));
        table.addCell(headerCellRight("Einzelpreis"));
        table.addCell(headerCellRight("Gesamtpreis"));
    }

    private PdfPTable footerTable(InvoiceProperties invoiceProperties, InvoiceSum invoiceSum) {
        log.info("creating footer table");
        PdfPTable table = new PdfPTable(7);
        table.setTotalWidth(A4.getWidth() - 100);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(ALIGN_CENTER);

        table.addCell(fineCell("Hinweis gemäß § 14 Absatz 4 Satz 1 Nr. 7 Umsatzsteuergesetz:", table.getNumberOfColumns()));
        table.addCell(fineCell("Zahlbar entsprechend Konditionenvereinbarung vom 30.7.2004", table.getNumberOfColumns()));

        addFooterRow(table, "Material", invoiceSum.getMaterials(), DEFAULT_FONT, TOP, NO_BORDER);
        addFooterRow(table, "Leistung", invoiceSum.getEfforts(), DEFAULT_FONT, NO_BORDER, NO_BORDER);
        addFooterRow(table, "Netto", invoiceSum.getTotal(), DEFAULT_FONT, NO_BORDER, TOP);
        BigDecimal mwst = calculatePercentage(invoiceSum.getTotal(), invoiceProperties.getMwstInPercentage());
        addFooterRow(table, "Mehrwertsteuer (7.00%)", mwst, DEFAULT_FONT, NO_BORDER, BOTTOM);
        addFooterRow(table, "Gesamtbetrag", invoiceSum.getTotal().add(mwst), BOLD_FONT, BOTTOM, NO_BORDER);

        table.addCell(fineCell("Umsatzsteuer-Identifikationsnummer DE239653548", table.getNumberOfColumns()));

        return table;
    }

}
