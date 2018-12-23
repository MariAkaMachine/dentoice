package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mariakamachine.dentoice.config.properties.InvoiceProperties;
import com.mariakamachine.dentoice.data.entity.DentistEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.itextpdf.text.Element.ALIGN_RIGHT;
import static com.itextpdf.text.FontFactory.*;
import static com.itextpdf.text.PageSize.A4;
import static com.itextpdf.text.Rectangle.NO_BORDER;
import static com.itextpdf.text.pdf.PdfWriter.getInstance;

@Slf4j
public class InvoicePdfGenerator {

    static final Font DEFAULT_FONT = getFont(COURIER, 10);
    static final Font BOLD_FONT = getFont(COURIER_BOLD, 10);
    static final Font SMALL_FONT = getFont(COURIER, 8);

    public byte[] generateMonthlyPdf(InvoiceProperties invoiceProperties, List<InvoiceEntity> invoices) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document pdf = new Document(A4, 50, 50, 110, 150);
        try {
            PdfWriter writer = getInstance(pdf, stream);
            pdf.open();
            writer.setPageEvent(new PdfPageNumberEvent(1L));
            pdf.add(recipientDetails(invoices.get(0).getDentist()));
            addTablesToPdf(pdf, writer, new MonthlyPdfInvoice().generateTables(invoiceProperties, invoices));
            pdf.close();
        } catch (DocumentException e) {
            log.error("unable to generate pdf invoice for monthly invoice", e);
        }
        return stream.toByteArray();

    }

    public byte[] generatePdf(InvoiceProperties invoiceProperties, InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document pdf = new Document(A4, 50, 50, 110, 150);
        try {
            PdfWriter writer = getInstance(pdf, stream);
            pdf.open();

            /*
             * REMOVE
             */
            invoice.setId(18120137L);
            /*
             * REMOVE
             */

            writer.setPageEvent(new PdfPageNumberEvent(invoice.getId()));

            pdf.add(recipientDetails(invoice.getDentist()));
            addTablesToPdf(pdf, writer, new PdfInvoice().generateTables(invoiceProperties, invoice));
            pdf.close();
        } catch (DocumentException e) {
            log.error("unable to generate pdf invoice for invoice {}", invoice.getId(), e);
        }
        return stream.toByteArray();
    }

    private void addTablesToPdf(Document pdf, PdfWriter writer, List<PdfPTable> tables) throws DocumentException {
        int counter = 1;
        for (PdfPTable table : tables) {
            if (counter == table.size()) {
                table.writeSelectedRows(0, -1, pdf.left(), table.getTotalHeight() + 45, writer.getDirectContent());
            }
            pdf.add(table);
            counter++;
        }
    }

    private PdfPTable recipientDetails(DentistEntity dentist) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(ALIGN_LEFT);

        /*
         * REMOVE
         */
        dentist = new DentistEntity();
        dentist.setTitle("Herr Zahnarzt");
        dentist.setFirstName("Halo I");
        dentist.setLastName("Bims");
        dentist.setStreet("Leinenweberstr. 47");
        dentist.setZip("70567");
        dentist.setCity("Stuttgart");
        /*
         * REMOVE
         */

        table.addCell(cell(dentist.getTitle()));
        table.addCell(cell(dentist.getFirstName() + " " + dentist.getLastName()));
        table.addCell(cell(dentist.getStreet()));
        table.addCell(cell(dentist.getZip() + " " + dentist.getCity()));

        return table;
    }

    /*
     * CELL FORMATTING
     */

    static PdfPCell headerCell(String text) {
        return marginCell(text, ALIGN_LEFT);
    }

    static PdfPCell headerCellRight(String text) {
        return marginCell(text, ALIGN_RIGHT);
    }

    static PdfPCell marginCell(String text, int alignment) {
        PdfPCell cell = cell(text, alignment, DEFAULT_FONT, 3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        return cell;
    }

    static PdfPCell cell(String text) {
        return cell(text, ALIGN_LEFT, NO_BORDER);
    }

    static PdfPCell cellRight(String text) {
        return cell(text, ALIGN_RIGHT, NO_BORDER);
    }

    static PdfPCell cell(String text, int border) {
        return cell(text, ALIGN_LEFT, border);
    }

    static PdfPCell cell(String text, int alignment, int border) {
        return cell(text, alignment, DEFAULT_FONT, border);
    }

    static PdfPCell fineCell(String text, int colSpan) {
        PdfPCell cell = cell(text, ALIGN_LEFT, SMALL_FONT, NO_BORDER);
        cell.setColspan(colSpan);
        return cell;
    }

    static PdfPCell cell(String text, int alignment, Font font, int border) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(alignment);
        cell.setPhrase(new Phrase(text, font));
        cell.setBorder(border);
        return cell;
    }

    /*
     * ROW FORMATTING
     */

    static void addEmptyRow(PdfPTable table) {
        PdfPCell cell = cell(" ");
        cell.setColspan(table.getNumberOfColumns());
        table.addCell(cell);
    }

    static void addFooterRow(PdfPTable table, String text, BigDecimal sum, Font font, int outerBorder, int innerBorder) {
        table.addCell(cell(" ", outerBorder));
        table.addCell(cell(" ", outerBorder));
        table.addCell(cell(" ", outerBorder));
        table.addCell(cell(" ", outerBorder));
        PdfPCell textCell = cell(text, ALIGN_LEFT, font, outerBorder + innerBorder);
        textCell.setColspan(2);
        table.addCell(textCell);
        table.addCell(cell(sum.toPlainString() + " €", ALIGN_RIGHT, font, outerBorder + innerBorder));
    }

}
